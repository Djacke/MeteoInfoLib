/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.math.linalg;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.meteoinfo.data.ArrayUtil;
import ucar.ma2.Array;
import ucar.ma2.DataType;

/**
 *
 * @author Yaqiang Wang
 */
public class LinalgUtil {
    /**
     * Solve a linear matrix equation, or system of linear scalar equations.
     * @param a Coefficient matrix.
     * @param b Ordinate or “dependent variable” values.
     * @return Solution to the system a x = b. Returned shape is identical to b.
     */
    public static Array solve(Array a, Array b){
        Array r = Array.factory(DataType.DOUBLE, b.getShape());
        double[][] aa = (double[][])ArrayUtil.copyToNDJavaArray(a);
        RealMatrix coefficients = new Array2DRowRealMatrix(aa, false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        double[] bb = (double[])ArrayUtil.copyToNDJavaArray(b);
        RealVector constants = new ArrayRealVector(bb, false);
        RealVector solution = solver.solve(constants);
        for (int i = 0; i < r.getSize(); i++){
            r.setDouble(i, solution.getEntry(i));
        }
        
        return r;
    }
    
    /**
     * Calculates the Cholesky decomposition of a matrix.
     * The Cholesky decomposition of a real symmetric positive-definite matrix A consists of a 
     * lower triangular matrix L with same size such that: A = LLT. In a sense, this is 
     * the square root of A.
     * @param a The given matrix.
     * @return Result array.
     */
    public static Array cholesky(Array a){
        Array r = Array.factory(DataType.DOUBLE, a.getShape());
        double[][] aa = (double[][])ArrayUtil.copyToNDJavaArray(a);
        RealMatrix matrix = new Array2DRowRealMatrix(aa, false);
        CholeskyDecomposition decomposition = new CholeskyDecomposition(matrix);
        RealMatrix L = decomposition.getL();
        int n = L.getColumnDimension();
        int m = L.getRowDimension();
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++){
                r.setDouble(i * n + j, L.getEntry(i, j));
            }
        }
        
        return r;
    }
    
    /**
     * Calculates the LUP-decomposition of a square matrix.
     * The LUP-decomposition of a matrix A consists of three matrices L, U and P that satisfy: 
     * P×A = L×U. L is lower triangular (with unit diagonal terms), U is upper triangular and P is 
     * a permutation matrix. All matrices are m×m.
     * @param a Given matrix.
     * @return Result P/L/U arrays.
     */
    public static Array[] lu(Array a){
        Array Pa = Array.factory(DataType.DOUBLE, a.getShape());
        Array La = Array.factory(DataType.DOUBLE, a.getShape());
        Array Ua = Array.factory(DataType.DOUBLE, a.getShape());
        double[][] aa = (double[][])ArrayUtil.copyToNDJavaArray(a);
        RealMatrix matrix = new Array2DRowRealMatrix(aa, false);
        LUDecomposition decomposition = new LUDecomposition(matrix);
        RealMatrix P = decomposition.getP();
        RealMatrix L = decomposition.getL();
        RealMatrix U = decomposition.getU();
        int n = L.getColumnDimension();
        int m = L.getRowDimension();
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++){
                Pa.setDouble(i * n + j, P.getEntry(i, j));
                La.setDouble(i * n + j, L.getEntry(i, j));
                Ua.setDouble(i * n + j, U.getEntry(i, j));
            }
        }
        
        return new Array[]{Pa, La, Ua};
    }
    
    /**
     * Calculates the QR-decomposition of a matrix.
     * The QR-decomposition of a matrix A consists of two matrices Q and R that satisfy: A = QR, Q 
     * is orthogonal (QTQ = I), and R is upper triangular. If A is m×n, Q is m×m and R m×n.
     * @param a Given matrix.
     * @return Result Q/R arrays.
     */
    public static Array[] qr(Array a){
        int m = a.getShape()[0];        
        int n = a.getShape()[1];
        Array Qa = Array.factory(DataType.DOUBLE, new int[]{m, m});
        Array Ra = Array.factory(DataType.DOUBLE, a.getShape());
        double[][] aa = (double[][])ArrayUtil.copyToNDJavaArray(a);
        RealMatrix matrix = new Array2DRowRealMatrix(aa, false);
        QRDecomposition decomposition = new QRDecomposition(matrix);
        RealMatrix Q = decomposition.getQ();
        RealMatrix R = decomposition.getR(); 
        for (int i = 0; i < m; i++){
            for (int j = 0; j < m; j++){
                Qa.setDouble(i * m + j, Q.getEntry(i, j));
            }
        }
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++){
                Ra.setDouble(i * n + j, R.getEntry(i, j));
            }
        }
        
        return new Array[]{Qa, Ra};
    }
    
    /**
     * Calculates the compact Singular Value Decomposition of a matrix.
     * The Singular Value Decomposition of matrix A is a set of three matrices: U, Σ and V 
     * such that A = U × Σ × VT. Let A be a m × n matrix, then U is a m × p orthogonal 
     * matrix, Σ is a p × p diagonal matrix with positive or null elements, V is a p × n 
     * orthogonal matrix (hence VT is also orthogonal) where p=min(m,n).
     * @param a Given matrix.
     * @return Result U/S/V arrays.
     */
    public static Array[] svd(Array a){
        int m = a.getShape()[0];        
        int n = a.getShape()[1];
        int k = Math.min(m, n);
        Array Ua = Array.factory(DataType.DOUBLE, new int[]{m, k});
        Array Va = Array.factory(DataType.DOUBLE, new int[]{k, n});
        Array Sa = Array.factory(DataType.DOUBLE, new int[]{k});
        double[][] aa = (double[][])ArrayUtil.copyToNDJavaArray(a);
        RealMatrix matrix = new Array2DRowRealMatrix(aa, false);
        SingularValueDecomposition decomposition = new SingularValueDecomposition(matrix);
        RealMatrix U = decomposition.getU();
        RealMatrix V = decomposition.getVT();     
        double[] sv = decomposition.getSingularValues();
        for (int i = 0; i < m; i++){
            for (int j = 0; j < k; j++){
                Ua.setDouble(i * k + j, U.getEntry(i, j));
            }
        }
        for (int i = 0; i < k; i++){
            for (int j = 0; j < n; j++){
                Va.setDouble(i * n + j, V.getEntry(i, j));
            }
        }
        for (int i = 0; i < k; i++){
            Sa.setDouble(i, sv[i]);
        }
        
        return new Array[]{Ua, Sa, Va};
    }
    
    /**
     * Calculates the eigen decomposition of a real matrix.
     * The eigen decomposition of matrix A is a set of two matrices: V and D such that 
     * A = V × D × VT. A, V and D are all m × m matrices.
     * @param a Given matrix.
     * @return Result W/V arrays.
     */
    public static Array[] eigen(Array a){
        int m = a.getShape()[0];        
        int n = a.getShape()[1];
        Array Wa = Array.factory(DataType.DOUBLE, new int[]{m});
        Array Va = Array.factory(DataType.DOUBLE, new int[]{m, n});
        double[][] aa = (double[][])ArrayUtil.copyToNDJavaArray(a);
        RealMatrix matrix = new Array2DRowRealMatrix(aa, false);
        EigenDecomposition decomposition = new EigenDecomposition(matrix);
        RealMatrix W = decomposition.getD();
        RealMatrix V = decomposition.getV();
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++){
                if (i == j)
                    Wa.setDouble(m - i - 1, W.getEntry(i, j));
                Va.setDouble((m - i - 1) * n + j, V.getEntry(i, j));
            }
        }        
        
        return new Array[]{Wa, Va};
    }
}
