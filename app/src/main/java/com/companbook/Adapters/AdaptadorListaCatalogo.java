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

public class AdaptadorListaCatalogo extends RecyclerView.Adapter<AdaptadorListaCatalogo.CatalogoViewHolder> implements View.OnClickListener {

    private ArrayList<Catalogo> lista;
    private View.OnClickListener listener;
    private Context contexto;

    public AdaptadorListaCatalogo(ArrayList<Catalogo> lista, Context context) {
        this.contexto = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdaptadorListaCatalogo.CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_catalogo, viewGroup, false);
        v.setOnClickListener(this);
        AdaptadorListaCatalogo.CatalogoViewHolder vh = new AdaptadorListaCatalogo.CatalogoViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaCatalogo.CatalogoViewHolder catalogoViewHolder, int i) {
        catalogoViewHolder.bindMensaje(lista.get(i));
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

    public class CatalogoViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvPrecio;
        ImageView fotoObj;

        public CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNomObjCat);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            fotoObj = itemView.findViewById(R.id.fotoObjCat);
        }

        public void bindMensaje(Catalogo m){
            tvNombre.setText(m.getNombre());
            tvPrecio.setText(m.getPrecio() + "");
            Glide.with(contexto).load(m.getUrl_foto()).into(fotoObj);
        }
    }

    public void clear(){
        lista.clear();
    }
}
