package adaptivetests.matrixmul;

import org.jlinalg.MatrixMultiplication;

/**
 * Created by pk250187 on 7/3/17.
 */
public class CustomizedMatrixMultiplication extends MatrixMultiplication {
    static {
        STRASSEN_ORIGINAL_TRUNCATION_POINT = 10;
        STRASSEN_WINOGRAD_TRUNCATION_POINT = 10;
        STRASSEN_BODRATO_TRUNCATION_POINT = 10;
    }
}
