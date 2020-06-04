package pt.kiko.krip.lang.results;

import pt.kiko.krip.lang.Token;
import pt.kiko.krip.lang.errors.Error;

import java.util.ArrayList;

public class LexResult {

    public Error error;
    public ArrayList<Token> list;

    public LexResult success(ArrayList<Token> list) {
        this.list = list;
        return this;
    }

    public LexResult failure(Error error) {
        this.error = error;
        return this;
    }

}
