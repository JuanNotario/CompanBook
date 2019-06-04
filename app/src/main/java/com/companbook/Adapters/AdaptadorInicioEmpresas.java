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
import com.companbook.Pojos.Catalogo;
import com.companbook.R;

import java.util.ArrayList;

public class AdaptadorInicioEmpresas extends RecyclerView.Adapter<AdaptadorInicioEmpresas.InicioViewHolder> implements View.OnClickListener {

    private ArrayList<Catalogo> lista;
    private View.OnClickListener listener;
    private Context contexto;

    public AdaptadorInicioEmpresas(ArrayList<Catalogo> lista, Context context) {
        this.contexto = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdaptadorInicioEmpresas.InicioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_inicio_empresas, viewGroup, false);
        v.setOnClickListener(this);
        AdaptadorInicioEmpresas.InicioViewHolder vh = new AdaptadorInicioEmpresas.InicioViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioEmpresas.InicioViewHolder inicioViewHolder, int i) {
        inicioViewHolder.bindMensaje(lista.get(i));
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

    public class InicioViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvPrecio;
        TextView tvDescripcion;
        ImageView fotoObj;

        public InicioViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoObj = itemView.findViewById(R.id.fotoIniCatalogo);
            tvNombre = itemView.findViewById(R.id.tvIniNombre);
            tvPrecio = itemView.findViewById(R.id.tvIniPrecio);
            tvDescripcion = itemView.findViewById(R.id.tvIniDesc);

        }

        public void bindMensaje(Catalogo m){
            tvNombre.setText(m.getNombre());
            tvPrecio.setText(m.getPrecio() + " €");
            tvDescripcion.setText(m.getDesc());
            Glide.with(contexto).load(m.getUrl_foto()).into(fotoObj);
        }
    }

    public void clear(){
        lista.clear();
    }
}
