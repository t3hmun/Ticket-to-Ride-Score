package com.t3hmun.tickettoridescore;

/**
 * Player colours.
 */
enum Colours {
    RED("Red"), YELLOW("Yellow"), GREEN("Green"), BLUE("Blue"), BLACK("Black");

    String text;

    Colours(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
