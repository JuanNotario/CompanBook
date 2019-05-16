package com.companbook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.companbook.Pojos.Datos_Empresa;
import com.companbook.R;

import java.util.ArrayList;

public class AdaptadorListaEmpresas extends RecyclerView.Adapter<AdaptadorListaEmpresas.EmpresasViewHolder> implements View.OnClickListener {

    private ArrayList<Datos_Empresa> lista;
    private View.OnClickListener listener;
    private Context contexto;

    public AdaptadorListaEmpresas(ArrayList<Datos_Empresa> lista, Context context) {
        this.contexto = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public EmpresasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_empresas, viewGroup, false);
        v.setOnClickListener(this);
        EmpresasViewHolder vh = new EmpresasViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresasViewHolder empresasViewHolder, int i) {
        empresasViewHolder.bindMensaje(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class EmpresasViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvDescripcion;
        ImageView logoComp;

        public EmpresasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreEmpresa);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionEmp);
            logoComp = itemView.findViewById(R.id.fotoUsuario);
        }

        public void bindMensaje(Datos_Empresa m){
            tvNombre.setText(m.getNombre());
            tvDescripcion.setText(m.getDescripcion());
            Glide.with(contexto).load(m.getUrl_logo()).into(logoComp);
        }
    }

    public void clear(){
        lista.clear();
    }
}
