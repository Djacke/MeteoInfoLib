/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.math;

import java.util.List;
import java.util.Random;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import ucar.ma2.Array;
import ucar.ma2.DataType;

/**
 *
 * @author Yaqiang Wang
 */
public class RandomUtil {
    /**
     * Get random value
     *
     * @return Random value
     */
    public static double rand() {
        Random r = new Random();
        return r.nextDouble();
    }

    /**
     * Get random array - one dimension
     *
     * @param n Array length
     * @return Result array
     */
    public static Array rand(int n) {
        Array r = Array.factory(DataType.DOUBLE, new int[]{n});
        Random rd = new Random();
        for (int i = 0; i < r.getSize(); i++) {
            r.setDouble(i, rd.nextDouble());
        }

        return r;
    }

    /**
     * Get random array
     *
     * @param shape Shape
     * @return Array Result array
     */
    public static Array rand(List<Integer> shape) {
        int[] ashape = new int[shape.size()];
        for (int i = 0; i < shape.size(); i++) {
            ashape[i] = shape.get(i);
        }
        Array a = Array.factory(DataType.DOUBLE, ashape);
        Random rd = new Random();
        for (int i = 0; i < a.getSize(); i++) {
            a.setDouble(i, rd.nextDouble());
        }

        return a;
    }
    
    /**
     * Get random value
     *
     * @return Random value
     */
    public static double randn() {
        Random r = new Random();
        return r.nextGaussian();
    }

    /**
     * Get random array - one dimension
     *
     * @param n Array length
     * @return Result array
     */
    public static Array randn(int n) {
        Array r = Array.factory(DataType.DOUBLE, new int[]{n});
        Random rd = new Random();
        for (int i = 0; i < r.getSize(); i++) {
            r.setDouble(i, rd.nextGaussian());
        }

        return r;
    }

    /**
     * Get random array
     *
     * @param shape Shape
     * @return Array Result array
     */
    public static Array randn(List<Integer> shape) {
        int[] ashape = new int[shape.size()];
        for (int i = 0; i < shape.size(); i++) {
            ashape[i] = shape.get(i);
        }
        Array a = Array.factory(DataType.DOUBLE, ashape);
        Random rd = new Random();
        for (int i = 0; i < a.getSize(); i++) {
            a.setDouble(i, rd.nextGaussian());
        }

        return a;
    }
}
