package com.github.superzhc.data.mining.smile;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.io.Read;
import smile.io.Write;

public class MySmileApplication {
    public static void main(String[] args) throws Exception {
        DataFrame df = Read.arff("xxx.arff");
        System.out.println(df);

        Formula formula = Formula.lhs("class");
        RandomForest rf= RandomForest.fit(formula,df);

        
    }
}
