package UIManagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class MACAddressDialog extends DialogFragment {

    private String macAddress;

    public interface MACAddressListener {
        public void onDialogPositiveClick(MACAddressDialog dialog);
    }

    MACAddressListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MACAddressListener) context;
        } catch (ClassCastException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(this.getActivity());
        builder.setMessage("Ingrese la dirección MAC en un formato válido:");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Establecer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Set MAC Address
                macAddress = input.getText().toString();
                listener.onDialogPositiveClick(MACAddressDialog.this);
            }
        });
        return builder.create();
    }

    public String getMacAddress() {
        return macAddress;
    }
}
