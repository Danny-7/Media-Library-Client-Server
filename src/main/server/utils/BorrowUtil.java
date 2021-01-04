package main.server.utils;

/** BorrowUtil : Util class for create a couple (Document, Subscriber)
 *  Used on borrows list in the LibraryService
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
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
