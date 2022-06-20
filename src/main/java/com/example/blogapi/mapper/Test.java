package com.example.blogapi.mapper;

public class Test {

    static int UniquePathHelper(int i, int j, int r, int c,
                                int[][] A)
    {
        // boundary condition or constraints
        if (i ==r || j == c) {
            return 0;
        }

        if (A[i][j] == 0) {
            return 0;
        }

        // base case
        if (A[i][j]==9) {
            return 1;
        }

        return Math.min(UniquePathHelper(i + 1, j, r, c, A),UniquePathHelper(i, j + 1, r, c, A));
    }

    public static void main(String[] args) {
        int[][] A
                = { { 1, 0, 0 }, { 1, 1, 0 }, { 0, 9, 0 } };
        int r = A.length, c = A[0].length;
        System.out.println(UniquePathHelper(0,0,r,c,A));
    }
}
