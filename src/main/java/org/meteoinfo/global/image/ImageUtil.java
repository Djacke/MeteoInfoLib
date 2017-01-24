 /* Copyright 2012 Yaqiang Wang,
 * yaqiang.wang@gmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 */
package org.meteoinfo.global.image;

import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author yaqiang
 */
public class ImageUtil {
    // <editor-fold desc="Variables">
    // </editor-fold>
    // <editor-fold desc="Constructor">
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Create gif animator file from image files
     *
     * @param inImageFiles Input image files
     * @param outGifFile Output gif file
     * @param delay Delay time in milliseconds between each frame
     * @param repeat Repeat times, 0 means unlimite repeat
     */
    public static void createGifAnimator(List<String> inImageFiles, String outGifFile, int delay, int repeat) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.setDelay(delay);
            e.start(outGifFile);
            for (String infn : inImageFiles){
                e.addFrame(ImageIO.read(new File(infn)));
            }
            e.finish();
        } catch (Exception e) {
            System.out.println("Create gif animator failed:");
            e.printStackTrace();
        }
    }
    
    /**
     * Create gif animator file from image files
     *
     * @param inImageFiles Input image files
     * @param outGifFile Output gif file
     * @param delay Delay time in milliseconds between each frame
     */
    public static void createGifAnimator(List<String> inImageFiles, String outGifFile, int delay) {
        createGifAnimator(inImageFiles, outGifFile, delay, 0);
    }
    
    /**
     * Create gif animator file from image files
     * @param infiles Input image files
     * @param outfile Output gif file
     * @param delay Delay time in milliseconds between each frame
     */
    public static void createGifAnimator(File[] infiles, File outfile, int delay) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.setDelay(delay);
            e.start(outfile.getCanonicalPath());
            for (File inf : infiles){
                e.addFrame(ImageIO.read(inf));
            }
            e.finish();
        } catch (Exception e) {
            System.out.println("Create gif animator failed:");
            e.printStackTrace();
        }
    }
    // </editor-fold>
}
