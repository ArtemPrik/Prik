package prik.lib.modules;

import prik.Console;
import prik.exceptions.ArgumentsMismatchException;
import prik.lib.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import prik.exceptions.TypeException;


/**
 *
 * @author Professional
 */
public final class files implements Module {
    private static Map<Integer, FileInfo> files;

    public static void initConstants() {
        Variables.define("FILES_COMPARATOR", new FunctionValue(new filesComparatorFunction()));
    }

    @Override
    public void init() {
        files = new HashMap<>();
        initConstants();

        Functions.set("fopen", new fopen());
        Functions.set("flush", new flush());
        Functions.set("fclose", new fclose());
        
        // Operations
        Functions.set("copy", new copy());
        Functions.set("create", new create());
        Functions.set("delete", fileToBoolean(File::delete));
        Functions.set("listFiles", new listFiles());
        Functions.set("mkdir", fileToBoolean(File::mkdir));
        Functions.set("mkdirs", fileToBoolean(File::mkdirs));
        Functions.set("rename", new rename());

        // Permissions and statuses
        Functions.set("canExecute", fileToBoolean(File::canExecute));
        Functions.set("canRead", fileToBoolean(File::canRead));
        Functions.set("canWrite", fileToBoolean(File::canWrite));
        Functions.set("isDirectory", fileToBoolean(File::isDirectory));
        Functions.set("isFile", fileToBoolean(File::isFile));
        Functions.set("isHidden", fileToBoolean(File::isHidden));
        Functions.set("setExecutable", new setExecutable());
        Functions.set("setReadable", new setReadable());
        Functions.set("setReadOnly", new setReadOnly());
        Functions.set("setWritable", new setWritable());

        Functions.set("exists", fileToBoolean(File::exists));
        Functions.set("fileSize", new fileSize());
        Functions.set("getParent", new getParent());
        Functions.set("lastModified", new lastModified());
        Functions.set("setLastModified", new setLastModified());

        // IO
        Functions.set("readBoolean", new readBoolean());
        Functions.set("readByte", new readByte());
        Functions.set("readBytes", new readBytes());
        Functions.set("readAllBytes", new readAllBytes());
        Functions.set("readChar", new readChar());
        Functions.set("readShort", new readShort());
        Functions.set("readInt", new readInt());
        Functions.set("readLong", new readLong());
        Functions.set("readFloat", new readFloat());
        Functions.set("readDouble", new readDouble());
        Functions.set("readUTF", new readUTF());
        Functions.set("readLine", new readLine());
        Functions.set("readText", new readText());
        Functions.set("writeBoolean", new writeBoolean());
        Functions.set("writeByte", new writeByte());
        Functions.set("writeBytes", new writeBytes());
        Functions.set("writeChar", new writeChar());
        Functions.set("writeShort", new writeShort());
        Functions.set("writeInt", new writeInt());
        Functions.set("writeLong", new writeLong());
        Functions.set("writeFloat", new writeFloat());
        Functions.set("writeDouble", new writeDouble());
        Functions.set("writeUTF", new writeUTF());
        Functions.set("writeLine", new writeLine());
        Functions.set("writeText", new writeText());
        
        // ZIP
        MapValue zip = new MapValue(5);
        zip.set("zip", this::zipWithMapper);
        zip.set("zipFiles", this::zipFiles);
        zip.set("unzip", this::unzip);
        zip.set("unzipFiles", this::unzipFiles);
        zip.set("listZipEntries", this::listZipEntries);
        
        //G-ZIP
//        Functions.set("gzip", this::gzipFile);
//        Functions.set("gzipBytes", this::gzipBytes);
//        Functions.set("ungzip", this::ungzipFile);
//        Functions.set("ungzipBytes", this::ungzipBytes);
    }

    private static class filesComparatorFunction implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.checkAtLeast(2, args.length);
            
            final int fd1 = args[0].asInt();
            final int fd2 = args[1].asInt();
            if (!files.containsKey(fd1)) {
                return NumberValue.of(files.containsKey(fd2) ? 1 : 0);
            }
            if (!files.containsKey(fd2)) {
                return NumberValue.of(files.containsKey(fd1) ? -1 : 0);
            }

            final File file1 = files.get(fd1).file;
            final File file2 = files.get(fd2).file;
            return NumberValue.of(file1.compareTo(file2));
        }
    }
    
    private static class fopen implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.checkAtLeast(1, args.length);
            
            final File file = Console.fileInstance(args[0].asString());
            try {
                if (args.length > 1) {
                    return process(file, args[1].asString().toLowerCase());
                }
                return process(file, "r");
            } catch (IOException ioe) {
                return NumberValue.MINUS_ONE;
            }
        }
        
        private Value process(File file, String mode) throws IOException {
            DataInputStream dis = null;
            BufferedReader reader = null;
            if (mode.contains("rb")) {
                dis = new DataInputStream(new FileInputStream(file));
            } else if (mode.contains("r")) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            }
            
            DataOutputStream dos = null;
            BufferedWriter writer = null;
            final boolean append = mode.contains("+");
            if (mode.contains("wb")) {
                dos = new DataOutputStream(new FileOutputStream(file, append));
            } else if (mode.contains("w")) {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
            }
            
            final int key = files.size();
            files.put(key, new FileInfo(file, dis, dos, reader, writer));
            return NumberValue.of(key);
        }
    }
    
    private abstract static class FileFunction implements Function {
        
        @Override
        public Value execute(Value... args) {
            if (args.length < 1) throw new ArgumentsMismatchException("File descriptor expected");
            final int key = args[0].asInt();
            try {
                return execute(files.get(key), args);
            } catch (IOException ioe) {
                return NumberValue.MINUS_ONE;
            }
        }
        
        protected abstract Value execute(FileInfo fileInfo, Value[] args) throws IOException;
    }

    private static class listFiles extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return ArrayValue.of(fileInfo.file.list());
        }
    }
    
    private static class create implements Function {

        @Override
        public Value execute(Value... args) {
            String path = args[0].asString();
            File file = new File(path);
            try {
                file.createNewFile();
            } catch (IOException e) {
                return new StringValue("Error: " + e.toString());
            }
            return NumberValue.MINUS_ONE;
        }
    }

    private static class copy implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            try {
                final FileInputStream is = new FileInputStream(fileFrom(args[0]));
                final FileOutputStream os = new FileOutputStream(fileFrom(args[1]));
                final FileChannel ic = is.getChannel();
                ic.transferTo(0, ic.size(), os.getChannel());
                is.close();
                os.close();
                return NumberValue.ONE;
            } catch (IOException ioe) {
                return NumberValue.MINUS_ONE;
            }
        }
    }

    private static class rename implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            return NumberValue.fromBoolean( fileFrom(args[0]).renameTo(fileFrom(args[1])) );
        }
    }
    
    private static class fileSize extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.file.length());
        }
    }

    private static class getParent extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final String parent = fileInfo.file.getParent();
            return new StringValue(parent == null ? "" : parent);
        }
    }

    private static class lastModified extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.file.lastModified());
        }
    }

    private static class setLastModified extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final long time;
            if (args[1].type() == Types.NUMBER) {
                time = ((NumberValue)args[1]).asLong();
            } else {
                time = (long) args[1].asNumber();
            }
            return NumberValue.fromBoolean(fileInfo.file.setLastModified(time));
        }
    }

    private static class setReadOnly extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.fromBoolean(fileInfo.file.setReadOnly());
        }
    }

    private static class setExecutable extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final boolean ownerOnly = (args.length < 3) || (args[2].asInt() != 0);
            return NumberValue.fromBoolean(
                    fileInfo.file.setExecutable(args[1].asInt() != 0, ownerOnly));
        }
    }

    private static class setReadable extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final boolean ownerOnly = (args.length < 3) || (args[2].asInt() != 0);
            return NumberValue.fromBoolean(
                    fileInfo.file.setReadable(args[1].asInt() != 0, ownerOnly));
        }
    }

    private static class setWritable extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final boolean ownerOnly = (args.length < 3) || (args[2].asInt() != 0);
            return NumberValue.fromBoolean(
                    fileInfo.file.setWritable(args[1].asInt() != 0, ownerOnly));
        }
    }
    
    private static class readBoolean extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.fromBoolean(fileInfo.dis.readBoolean());
        }
    }
    
    private static class readByte extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readByte());
        }
    }
    
    private static class readBytes extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final ArrayValue array = (ArrayValue) args[1];
            int offset = 0, length = array.size();
            if (args.length > 3) {
                offset = args[2].asInt();
                length = args[3].asInt();
            }
            
            final byte[] buffer = new byte[length];
            final int readed = fileInfo.dis.read(buffer, offset, length);
            for (int i = 0; i < readed; i++) {
                array.set(i, NumberValue.of(buffer[i]));
            }
            return NumberValue.of(readed);
        }
    }
    
    private static class readAllBytes extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final int bufferSize = 4096;
            final byte[] buffer = new byte[bufferSize];
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int readed;
            while ((readed = fileInfo.dis.read(buffer, 0, bufferSize)) != -1) {
                baos.write(buffer, 0, readed);
            }
            baos.flush();
            baos.close();
            return ArrayValue.of(baos.toByteArray());
        }
    }
    
    private static class readChar extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of((short)fileInfo.dis.readChar());
        }
    }
    
    private static class readShort extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readShort());
        }
    }
    
    private static class readInt extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readInt());
        }
    }
    
    private static class readLong extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readLong());
        }
    }
    
    private static class readFloat extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readFloat());
        }
    }
    
    private static class readDouble extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return NumberValue.of(fileInfo.dis.readDouble());
        }
    }
    
    private static class readUTF extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return new StringValue(fileInfo.dis.readUTF());
        }
    }
    
    private static class readLine extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            return new StringValue(fileInfo.reader.readLine());
        }
    }
    
    private static class readText extends FileFunction {

        private static final int BUFFER_SIZE = 4096;

        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final StringBuilder result = new StringBuilder();
            final char[] buffer = new char[BUFFER_SIZE];
            int readed;
            while ((readed = fileInfo.reader.read(buffer, 0, BUFFER_SIZE)) != -1) {
                result.append(buffer, 0, readed);
            }
            return new StringValue(result.toString());
        }
    }
    
    
    private static class writeBoolean extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeBoolean(args[1].asInt() != 0);
            return NumberValue.ONE;
        }
    }
    
    private static class writeByte extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeByte((byte) args[1].asInt());
            return NumberValue.ONE;
        }
    }

    private static class writeBytes extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final ArrayValue array = (ArrayValue) args[1];
            int offset = 0, length = array.size();
            final byte[] bytes = new byte[length];
            for (int i = 0; i < length; i++) {
                bytes[i] = (byte) (array.get(i).asInt() & 0xFF);
            }
            if (args.length > 3) {
                offset = args[2].asInt();
                length = args[3].asInt();
            }
            fileInfo.dos.write(bytes, offset, length);
            return NumberValue.ONE;
        }
    }
    
    private static class writeChar extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final char ch = (args[1].type() == Types.NUMBER)
                    ? ((char) args[1].asInt())
                    : args[1].asString().charAt(0);
            fileInfo.dos.writeChar(ch);
            return NumberValue.ONE;
        }
    }
    
    private static class writeShort extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeShort((short) args[1].asInt());
            return NumberValue.ONE;
        }
    }
    
    private static class writeInt extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeInt(args[1].asInt());
            return NumberValue.ONE;
        }
    }
    
    private static class writeLong extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final long value;
            if (args[1].type() == Types.NUMBER) {
                value = ((NumberValue)args[1]).asLong();
            } else {
                value = (long) args[1].asNumber();
            }
            fileInfo.dos.writeLong(value);
            return NumberValue.ONE;
        }
    }
    
    private static class writeFloat extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            final float value;
            if (args[1].type() == Types.NUMBER) {
                value = ((NumberValue)args[1]).asFloat();
            } else {
                value = (float) args[1].asNumber();
            }
            fileInfo.dos.writeFloat(value);
            return NumberValue.ONE;
        }
    }
    
    private static class writeDouble extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeDouble(args[1].asNumber());
            return NumberValue.ONE;
        }
    }
    
    private static class writeUTF extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.dos.writeUTF(args[1].asString());
            return NumberValue.ONE;
        }
    }
    
    private static class writeLine extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.writer.write(args[1].asString());
            fileInfo.writer.newLine();
            return NumberValue.ONE;
        }
    }

    private static class writeText extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            fileInfo.writer.write(args[1].asString());
            return NumberValue.ONE;
        }
    }
    
    private static class flush extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            if (fileInfo.dos != null) {
                fileInfo.dos.flush();
            }
            if (fileInfo.writer != null) {
                fileInfo.writer.flush();
            }
            return NumberValue.ONE;
        }
    }
    
    private static class fclose extends FileFunction {
        @Override
        protected Value execute(FileInfo fileInfo, Value[] args) throws IOException {
            if (fileInfo.dis != null) {
                fileInfo.dis.close();
            }
            if (fileInfo.dos != null) {
                fileInfo.dos.close();
            }
            if (fileInfo.reader != null) {
                fileInfo.reader.close();
            }
            if (fileInfo.writer != null) {
                fileInfo.writer.close();
            }
            return NumberValue.ONE;
        }
    }

    private static File fileFrom(Value value) {
        if (value.type() == Types.NUMBER) {
            return files.get(value.asInt()).file;
        }
        return Console.fileInstance(value.asString());
    }

    private interface FileToBooleanFunction {

        boolean apply(File file);
    }

    private static Function fileToBoolean(FileToBooleanFunction f) {
        return args -> {
            Arguments.check(1, args.length);
            return NumberValue.fromBoolean(f.apply(fileFrom(args[0])));
        };
    }
    
    private static class FileInfo {
        File file;
        DataInputStream dis;
        DataOutputStream dos;
        BufferedReader reader;
        BufferedWriter writer;

        public FileInfo(File file, DataInputStream dis, DataOutputStream dos, BufferedReader reader, BufferedWriter writer) {
            this.file = file;
            this.dis = dis;
            this.dos = dos;
            this.reader = reader;
            this.writer = writer;
        }
    }
    
    /////////////////////////////////////////
    /// ZIP
    /////////////////////////////////////////
    
    private Value zipWithMapper(Value[] args) {
        Arguments.checkOrOr(2, 3, args.length);
        final String inputPath = args[0].asString();
        final File input = new File(inputPath);
        if (!input.exists()) {
            return NumberValue.MINUS_ONE;
        }
        final File output = new File(args[1].asString());
        if (output.isDirectory()) {
            return NumberValue.MINUS_ONE;
        }
        final Function mapper = getMapperOrNull(args, 2);
        
        final Map<File, String> mappings = new HashMap<>();
        final String rootPath = (input.isFile() ? input.getParent() : input.getAbsolutePath());
        generateFileList(mappings, rootPath, input, mapper);
        return zipFileList(mappings, output);
    }
    
    private Value zipFiles(Value[] args) {
        Arguments.check(2, args.length);
        
        final Map<File, String> mappings = new HashMap<>();
        switch (args[0].type()) {
            case Types.STRING: {
                final File file = new File(args[0].asString());
                mappings.put(file, file.getName());
            } break;
            case Types.ARRAY:
                for (Value value : ((ArrayValue) args[0])) {
                    final File file = new File(value.asString());
                    mappings.put(file, file.getName());
                }
                break;
            case Types.MAP:
                for (Map.Entry<Value, Value> entry : ((MapValue) args[0])) {
                    final File file = new File(entry.getKey().asString());
                    mappings.put(file, entry.getValue().asString());
                }
                break;
            default:
                throw new TypeException("Single file path, file paths array or file mappings expected at first argument");
        }
        
        final File output = new File(args[1].asString());
        if (output.isDirectory()) {
            return NumberValue.MINUS_ONE;
        }
        return zipFileList(mappings, output);
    }
    
    private Value zipFileList(Map<File, String> mappings, File output) {
        int count = 0;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(output))) {
            for (Map.Entry<File, String> entry : mappings.entrySet()) {
                final File node = entry.getKey();
                final String entryName = entry.getValue();
                if (node.isDirectory()) {
                    zos.putNextEntry(new ZipEntry(entryName + (entryName.endsWith("/") ? "" : "/")));
                } else {
                    zipFile(zos, entry.getKey(), entry.getValue());
                    count++;
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("zip files", ex);
        }
        return NumberValue.of(count);
    }

    private void zipFile(ZipOutputStream zos, File file, String entryPath) throws IOException {
        final ZipEntry entry = new ZipEntry(entryPath);
        entry.setTime(file.lastModified());
        zos.putNextEntry(entry);
        try (FileInputStream fis = new FileInputStream(file)) {
            copy(fis, zos);
        }
    }

    private Value unzip(Value[] args) {
        Arguments.checkOrOr(2, 3, args.length);
        final File input = new File(args[0].asString());
        if (!input.exists() || !input.canRead() || input.isDirectory()) {
            return NumberValue.MINUS_ONE;
        }
        final File output = new File(args[1].asString());
        if (!output.exists()) {
            output.mkdirs();
        }
        final Function mapper = getMapperOrNull(args, 2);
        
        final Map<String, File> mappings = new HashMap<>();
        for (String entryName : listEntries(input)) {
            String fileName = entryName;
            if (mapper != null) {
                fileName = mapper.execute(new StringValue(entryName)).asString();
            }
            if (!fileName.isEmpty()) {
                mappings.put(entryName, new File(output, fileName));
            }
        }
        return unzipFileList(input, mappings);
    }
    
    private Value unzipFiles(Value[] args) {
        Arguments.check(2, args.length);
        
        final File input = new File(args[0].asString());
        if (!input.exists() || !input.canRead() || input.isDirectory()) {
            return NumberValue.MINUS_ONE;
        }
        
        final Map<String, File> mappings = new HashMap<>();
        switch (args[1].type()) {
            case Types.STRING: {
                final String entryPath = args[1].asString();
                mappings.put(entryPath, new File(entryPath));
            } break;
            case Types.ARRAY:
                for (Value value : ((ArrayValue) args[1])) {
                    final String entryPath = value.asString();
                    mappings.put(entryPath, new File(entryPath));
                }
                break;
            case Types.MAP:
                for (Map.Entry<Value, Value> entry : ((MapValue) args[1])) {
                    final File file = new File(entry.getValue().asString());
                    mappings.put(entry.getKey().asString(), file);
                }
                break;
            default:
                throw new TypeException("Single entry path, entry paths array or entry mappings expected at second argument");
        }
        return unzipFileList(input, mappings);
    }

    private Value unzipFileList(File input, Map<String, File> mappings) {
        int count = 0;
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(input))) {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                final String entryName = ze.getName();
                final File file = mappings.get(entryName);
                if (file == null) continue;
                
                if (entryName.endsWith("/")) {
                    safeMkdirs(file);
                } else {
                    safeMkdirs(file.getParentFile());
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        copy(zis, fos);
                    }
                    if (ze.getTime() > 0) {
                        file.setLastModified(ze.getTime());
                    }
                    count++;
                }
            }
            zis.closeEntry();
        } catch (IOException ex) {
            throw new RuntimeException("unzip files", ex);
        }
        return NumberValue.of(count);
    }
    
    private Value listZipEntries(Value[] args) {
        Arguments.check(1, args.length);
        final File input = new File(args[0].asString());
        if (!input.exists() || !input.canRead() || input.isDirectory()) {
            return new ArrayValue(0);
        }
        return ArrayValue.of(listEntries(input));
    }
    
    private Function getMapperOrNull(Value[] args, int index) {
        Function mapper;
        if (args.length >= (index + 1)) {
            mapper = ValueUtils.consumeFunction(args[index], index);
        } else {
            mapper = null;
        }
        return mapper;
    }
    
    private void copy(InputStream is, OutputStream os) throws IOException {
        final byte[] buffer = new byte[8192];
        int read;
        while ((read = is.read(buffer)) != -1) {
            os.write(buffer, 0, read);
        }
    }
    
    private void generateFileList(Map<File, String> mappings, String rootPath, File node, Function mapper) {
        if (!rootPath.equals(node.getAbsolutePath())) {
            String entryPath = node.getAbsolutePath().substring(rootPath.length() + 1);
            if (mapper != null) {
                entryPath = mapper.execute(new StringValue(entryPath)).asString();
                if (entryPath.isEmpty()) {
                    return;
                }
            }
            mappings.put(node, entryPath);
        }
        
        if (node.isDirectory()) {
            for (File file : node.listFiles()) {
                generateFileList(mappings, rootPath, file, mapper);
            }
        }
    }
    
    private String[] listEntries(File input) {
        final List<String> entries = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(input))) {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                entries.add(ze.getName());
            }
            zis.closeEntry();
        } catch (IOException ex) {
            throw new RuntimeException("list zip entries", ex);
        }
        return entries.toArray(new String[0]);
    }

    private void safeMkdirs(File file) {
        if (file != null) {
            file.mkdirs();
        }
    }
}
