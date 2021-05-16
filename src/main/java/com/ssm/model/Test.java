package com.ssm.model;

/**
 * @author Eizooo
 * @date 2021/4/21 18:04
 */
public class Test {
    private Integer age = 10;
    private String name = "lw";

    public Test() {
    }


    public Test(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
