package Aaron;

import Sean.Christopher;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Scope;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class Michael implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final Object createFromParcel(Parcel parcel) {
        int Robert2 = Christopher.Robert(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        Uri uri = null;
        String str5 = null;
        String str6 = null;
        ArrayList arrayList = null;
        String str7 = null;
        String str8 = null;
        long j = 0;
        int i = 0;
        while (parcel.dataPosition() < Robert2) {
            int readInt = parcel.readInt();
            switch ((char) readInt) {
                case 1:
                    i = Christopher.Amanda(parcel, readInt);
                    break;
                case 2:
                    str = Christopher.Christopher(parcel, readInt);
                    break;
                case 3:
                    str2 = Christopher.Christopher(parcel, readInt);
                    break;
                case 4:
                    str3 = Christopher.Christopher(parcel, readInt);
                    break;
                case 5:
                    str4 = Christopher.Christopher(parcel, readInt);
                    break;
                case 6:
                    uri = (Uri) Christopher.Michael(parcel, readInt, Uri.CREATOR);
                    break;
                case 7:
                    str5 = Christopher.Christopher(parcel, readInt);
                    break;
                case '\b':
                    j = Christopher.Daniel(parcel, readInt);
                    break;
                case '\t':
                    str6 = Christopher.Christopher(parcel, readInt);
                    break;
                case '\n':
                    arrayList = Christopher.Matthew(parcel, readInt, Scope.CREATOR);
                    break;
                case 11:
                    str7 = Christopher.Christopher(parcel, readInt);
                    break;
                case '\f':
                    str8 = Christopher.Christopher(parcel, readInt);
                    break;
                default:
                    Christopher.James(parcel, readInt);
                    break;
            }
        }
        Christopher.Ashley(parcel, Robert2);
        return new GoogleSignInAccount(i, str, str2, str3, str4, uri, str5, j, str6, arrayList, str7, str8);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i) {
        return new GoogleSignInAccount[i];
    }
}