package xyz.sistemagte.gte.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import xyz.sistemagte.gte.Construtoras.FuncConst;
import xyz.sistemagte.gte.Construtoras.MensalidadeConst;
import xyz.sistemagte.gte.R;


public class ListViewMensalidades extends ArrayAdapter <MensalidadeConst> {

    private List<MensalidadeConst> mensa;

    private Context mCtx;

    public ListViewMensalidades(List<MensalidadeConst> mensa, Context mCtx){
        super(mCtx, R.layout.list_view_mensalidades, mensa);
        this.mensa = mensa;
        this.mCtx = mCtx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_mensalidades, null, true);

        TextView txtNomeResp = listViewItem.findViewById(R.id.txtNomeResp);
        TextView txtStatus = listViewItem.findViewById(R.id.txtStatus);
        TextView txtValor = listViewItem.findViewById(R.id.txtValor);
        TextView txtCrianca = listViewItem.findViewById(R.id.txtCrianca);



        MensalidadeConst mensalidadeConst = mensa.get(position);

        if(Integer.parseInt(mensalidadeConst.getStatus()) == 0){
            txtStatus.setText(mCtx.getResources().getString(R.string.pendente));
        }
        if(Integer.parseInt(mensalidadeConst.getStatus()) == 1){
            txtStatus.setText(mCtx.getResources().getString(R.string.pago));
        }
        if(Integer.parseInt(mensalidadeConst.getStatus()) == 2){
            txtStatus.setText(mCtx.getResources().getString(R.string.atrasada));
        }

        txtNomeResp.setText(mensalidadeConst.getNomeResp() + " " + mensalidadeConst.getSobreResp());
        txtValor.setText((mCtx.getResources().getString(R.string.real)+ " " + String.valueOf(mensalidadeConst.getValor())));
        txtCrianca.setText((mCtx.getResources().getString(R.string.criancaEspaço)+ " " + mensalidadeConst.getNomeCrianca() + " " + mensalidadeConst.getSobreCrianca()));

        return listViewItem;
    }
}
