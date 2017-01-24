/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

/**
 *
 * @author wyq
 */
public class IOUtil {

    /**
     * Get file chart
     *
     * @param filePath File path
     * @return File chart
     * @throws IOException
     */
    public static String getFileChart(String filePath) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(
                new FileInputStream(filePath));
        int p = (bin.read() << 8) + bin.read();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }

        return code;

    }
    
}
