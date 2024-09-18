package com.example.mobile_todo

class Task() {
    private var id: Int = 0;
    private var text: String = "";
    private var status: Int = 0;

    fun getId(): Int {
        return this.id
    }
    fun getText(): String{
        return this.text
    }
    fun getStatus(): Int {
        return this.status
    }
    fun setId(id: Int){
        this.id = id;
    }
    fun setText(text: String) {
        this.text = text;
    }
    fun setStatus(status: Int) {
        this.status = status;
    }
}