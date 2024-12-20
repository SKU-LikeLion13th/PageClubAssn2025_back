package likelion13.page.domain;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum RentStatus {
    BOOK, RENT, RETURN, CANCEL, DELAY_RETURN, LONG_DELAY_RETURN;

    public boolean isUsingGroup() {
        return usingGroup.contains(this);
    }
    public static final Set<RentStatus> usingGroup = Collections.unmodifiableSet(EnumSet.of(RentStatus.RENT, RentStatus.BOOK));


    public boolean isCanPenaltyGroup() {
        return canPenaltyGroup.contains(this);
    }
    public static final Set<RentStatus> canPenaltyGroup = Collections.unmodifiableSet(EnumSet.of(RentStatus.RENT, RentStatus.DELAY_RETURN, RentStatus.LONG_DELAY_RETURN));



}
