package exceptions;

import java.io.IOException;

public class ManagerSaveException extends Exception {
    public ManagerSaveException(Throwable e) {
        super(e);
    }

    // todo понять нужно ли тут что-то дописывать
}
