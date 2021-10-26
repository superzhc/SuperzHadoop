/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.json.generator.types;

import com.github.javafaker.Faker;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Locale;

/**
 *
 * @author andrewserff
 */
public abstract class TypeHandler {
    private RandomDataGenerator rand;
    private Faker faker;
    private String[] launchArguments;
    
    public TypeHandler() {
        rand = new RandomDataGenerator();
        faker=new Faker(new Locale("zh-CN"));
    }
    
    public abstract Object getNextRandomValue();
    public abstract String getName();
    
    /**
     * @return the rand
     */
    public RandomDataGenerator getRand() {
        return rand;
    }

    /**
     * @param rand the rand to set
     */
    public void setRand(RandomDataGenerator rand) {
        this.rand = rand;
    }

    public Faker getFaker() {
        return faker;
    }

    public void setFaker(Faker faker) {
        this.faker = faker;
    }

    /**
     * @return the launchArguments
     */
    public String[] getLaunchArguments() {
        return launchArguments;
    }

    /**
     * @param launchArguments the launchArguments to set
     */
    public void setLaunchArguments(String[] launchArguments) {
        this.launchArguments = launchArguments;
    }
    
    public static String stripQuotes(String s) {
        // 2021年10月26日 参数中包含两个单引号的代表是转义，这个要保留，将 ★ 作为关键符号
        return s.replaceAll("''","★").replaceAll("'", "").replaceAll("\"", "").replaceAll("★","'").trim();
    }
}
