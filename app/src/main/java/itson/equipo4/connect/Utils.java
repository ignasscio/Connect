package itson.equipo4.connect;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    /**
     * Having something such as: Toast.makeText(this,"Error al subir imagen de perfil. "+e.message,Toast.LENGTH_SHORT).show()
     * can become quite messy, especially if it's used multiple times in a single method.
     *
     * This simplifies the show-ening of toasts by just needing to call Utils.displayShortToast("Message", context))
     * Using this method will display a toast for just a short amount of time.
     *
     * @param str the param str is the message that will be displayed in the toast
     * @param context the param context is obviously the context for the toast
     */
    public static void displayShortToast(String str, Context context) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * Having something such as: Toast.makeText(this,"Error al subir imagen de perfil. "+e.message,Toast.LENGTH_LONG).show()
     * can become quite messy, especially if it's used multiple times in a single method.
     *
     * This simplifies the show-ening of toasts by just needing to call Utils.displayLongToast("Message", context))
     * Using this method will display a toast for longer than displayShortToast() does.
     *
     * @param str the param str is the message that will be displayed in the toast
     * @param context the param context is obviously the context for the toast
     */
    public static void displayLongToast(String str, Context context) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
