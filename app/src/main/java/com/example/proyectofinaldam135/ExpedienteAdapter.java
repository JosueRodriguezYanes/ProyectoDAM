package com.example.proyectofinaldam135;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

    public class ExpedienteAdapter extends RecyclerView.Adapter<ExpedienteAdapter.ExpedienteViewHolder> {

        public interface OnItemClickListener {
            void onItemClick(Expediente expediente);
        }

        private List<Expediente> expedientes;
        private OnItemClickListener listener;

        public ExpedienteAdapter(List<Expediente> expedientes, OnItemClickListener listener) {
            this.expedientes = expedientes != null ? expedientes : new ArrayList<>();
            this.listener = listener;
        }


    @NonNull
    @Override
    public ExpedienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_expediente, parent, false);
            return new ExpedienteViewHolder(view);
        } catch (Exception e) {
            Log.e("ADAPTER_ERROR", "Error al crear ViewHolder", e);
            throw new RuntimeException("Error al inflar layout del item", e);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ExpedienteViewHolder holder, int position) {
        try {
            Expediente expediente = expedientes.get(position);
            holder.bind(expediente);

            holder.itemView.setOnClickListener(v ->
            {
                if (listener != null)
                {
                    listener.onItemClick(expediente);
                }
            });

        } catch (Exception e) {
            Log.e("ADAPTER_ERROR", "Error en posición " + position, e);
        }
    }

    @Override
    public int getItemCount() {
        return expedientes.size();
    }

    static class ExpedienteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsuario, tvCliente, tvProblema, tvFecha, tvEstado;

        public ExpedienteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de incluir tvUsuario aquí
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvProblema = itemView.findViewById(R.id.tvProblema);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }

        public void bind(Expediente expediente) {
            // Ahora puedes usar tvUsuario sin errores
            tvUsuario.setText("Usuario: " + expediente.getUsuario());
            tvCliente.setText(expediente.getCliente());
            tvProblema.setText(expediente.getProblema());
            tvFecha.setText(expediente.getFecha());
            tvEstado.setText(expediente.getUrgencia());

            // Resaltar urgencia
            if ("Urgente".equals(expediente.getUrgencia())) {
                tvEstado.setBackgroundResource(R.drawable.bg_urgencia_urgente);
                tvEstado.setTextColor(Color.WHITE);
            } else {
                tvEstado.setBackgroundResource(R.drawable.bg_urgencia);
                tvEstado.setTextColor(Color.BLACK);
            }
        }
    }
}