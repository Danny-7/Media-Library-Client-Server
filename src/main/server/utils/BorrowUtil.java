package main.server.utils;

public class BorrowUtil {
    private final Integer docNumber;
    private final Integer subNumber;

    public BorrowUtil(Integer docNumber, Integer subNumber) {
        this.docNumber = docNumber;
        this.subNumber = subNumber;
    }

    public Integer getDocNumber() {
        return docNumber;
    }

    public Integer getSubNumber() {
        return subNumber;
    }
}
