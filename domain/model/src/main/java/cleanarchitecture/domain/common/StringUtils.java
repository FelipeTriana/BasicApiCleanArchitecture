package cleanarchitecture.domain.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }

    public static boolean isEmpty(String... strings){
        boolean empty = false;
        for(String str : strings){
            empty = empty || isEmpty(str);
        }
        return empty;
    }

}
