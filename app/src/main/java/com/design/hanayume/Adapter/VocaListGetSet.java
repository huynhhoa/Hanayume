package com.design.hanayume.Adapter;

public class VocaListGetSet {
    private int id;
    private String Kanji;
    private String TuHan;
    private String Description;
    private String Meaning;
    private String Hiragana;
    private boolean Remember;
    private String Example1;
    private String Example4;
    private String Question1;
    private String Question2;
    private String Question3;
    public VocaListGetSet(int id, String kanji, String tuHan,String description, String meaning, String hiragana, boolean remember, String example1, String example4, String question1, String question2, String question3) {
        this.id = id;
        Kanji = kanji;
        TuHan = tuHan;
        Description = description;
        Meaning = meaning;
        Hiragana = hiragana;
        Remember = remember;
        Example1 = example1;
        Example4 = example4;
        Question1 = question1;
        Question2 = question2;
        Question3 = question3;
    }
    public VocaListGetSet(int id, String kanji, String tuHan, boolean remember){
        this.id = id;
        Kanji = kanji;
        TuHan = tuHan;
        Remember = remember;
    }

    public String getExample4() {
        return Example4;
    }

    public void setExample4(String example4) {
        Example4 = example4;
    }

    public String getQuestion1() {
        return Question1;
    }

    public void setQuestion1(String question1) {
        Question1 = question1;
    }

    public String getQuestion2() {
        return Question2;
    }

    public void setQuestion2(String question2) {
        Question2 = question2;
    }

    public String getQuestion3() {
        return Question3;
    }

    public void setQuestion3(String question3) {
        Question3 = question3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKanji() {
        return Kanji;
    }

    public void setKanji(String kanji) {
        Kanji = kanji;
    }

    public String getTuHan() {
        return TuHan;
    }

    public void setTuHan(String tuHan) {
        TuHan = tuHan;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMeaning() {
        return Meaning;
    }

    public void setMeaning(String meaning) {
        Meaning = meaning;
    }

    public String getHiragana() {
        return Hiragana;
    }

    public void setHiragana(String hiragana) {
        Hiragana = hiragana;
    }

    public boolean isRemember() {
        return Remember;
    }

    public void setRemember(boolean remember) {
        Remember = remember;
    }

    public String getExample1() {
        return Example1;
    }

    public void setExample1(String example1) {
        Example1 = example1;
    }

}
