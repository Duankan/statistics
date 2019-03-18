package com.dankin.utils;

/**
 * @desc 网页转图片处理类，使用外部CMD
 * @author dankin
 * @date 2019-03-16
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class PhantomTools {

    private static final Logger _logger = LoggerFactory.getLogger(PhantomTools.class);

    // private static final String _tempPath = "/data/temp/phantom_";
    // private static final String _shellCommand = "/usr/local/xxx/phantomjs /usr/local/xxx/rasterize.js ";  Linux下的命令
//    private static final String _tempPath = "C:\\Users\\ljiu\\Desktop\\image\\phantom_";
//    private static final String _shellCommand = "E:\\phantomjs-1.9.2-windows\\phantomjs E:\\phantomjs-1.9.2-windows\\rasterize.js ";
    private String _shellCommand;
    private String _file;
    private String _size;

    /**
     * @parm hash 用于临时文件的目录唯一化
     * @desc 构造截图类
     */
    public PhantomTools(int hash, String _tempPath, String _shellCommanddir) {
        try {
            _file = _tempPath + hash + ".png";
            _shellCommand = _shellCommanddir;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param size          图片的大小，如800px*600px（此时高度会裁切），或800px（此时 高度最少=宽度*9/16，高度不裁切）
     * @param _tempPath     图片的临时保存地址
     * @param _shellCommand phantomjs文件脚本执行的路径
     * @parm hash 用于临时文件的目录唯一化
     * @desc 构造截图类
     */
    public PhantomTools(int hash, String size, String _tempPath, String _shellCommand) {
        this(hash, _tempPath, _shellCommand);
        if (size != null)
            _size = " " + size;
    }

    /**
     * @param url 目标网页地址
     * @return 图片保存路径
     * @desc 将目标网页转为图片字节流
     */
    public String getByteImg(String url) throws IOException {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        File file = null;
        byte[] ret = null;
        try {
            if (exeCmd(_shellCommand + url + " " + _file + (_size != null ? _size : ""))) {
                file = new File(_file);
                if (file.exists()) {
                    out = new ByteArrayOutputStream();
                    byte[] b = new byte[5120];
                    in = new BufferedInputStream(new FileInputStream(file));
                    int n;
                    while ((n = in.read(b, 0, 5120)) != -1) {
                        out.write(b, 0, n);
                    }
                    file.delete();
                    ret = out.toByteArray();
                }
            } else {
                ret = new byte[]{};
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                _logger.error(e.getMessage());
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                _logger.error(e.getMessage());
            }
            if (file != null && file.exists()) {
//                file.delete();
            }
        }
        return _file;
    }

    /**
     * @param commandStr
     * @return false/true
     * @desc 执行CMD命令
     */
    private static boolean exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            if (p.waitFor() != 0 && p.exitValue() == 1) {
                return false;
            }
        } catch (Exception e) {
            _logger.error(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    _logger.error(e.getMessage());
                }
            }
        }
        return true;
    }
}
