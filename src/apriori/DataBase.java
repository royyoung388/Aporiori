package apriori;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class DataBase {
    private String filePath;

    public DataBase(String filePath) {
        this.filePath = filePath;
    }

    public byte[][] readData() {
        return null;
    }

    public void writeData(byte[][] data) {
        // 构建指定文件
//        File file = new File(filePath);
//        // 根据文件创建文件的输出流
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            for (int)
//                // 把内容转换成字符数组
//                char[] data = str.toCharArray();
//            // 向文件写入内容
//            writer.write(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
