package com.example.proyectofinaldam135;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpedienteAdapter extends RecyclerView.Adapter<ExpedienteAdapter.ExpedienteViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Expediente expediente);
    }

    public List<Expediente> expedientes;
    private OnItemClickListener listener;
    private List<String> expedientesRealizados = new ArrayList<>(); // Lista simple para realizados

    public ExpedienteAdapter(List<Expediente> expedientes, OnItemClickListener listener) {
        this.expedientes = expedientes != null ? expedientes : new ArrayList<>();
        this.listener = listener;
    }

    // Aqui esta el metodo para marcar realizado josue, revisa si te parece bien, sino, me rindo JAJA
    public void marcarComoRealizado(String cliente, String fecha, String problema) {
        String identificador = cliente + fecha + problema;
        if (!expedientesRealizados.contains(identificador)) {
            expedientesRealizados.add(identificador);
            notifyDataSetChanged();
        }
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

            // Configurar el estado basado en la urgencia
            if ("Realizado".equals(expediente.getUrgencia())) {
                holder.tvEstado.setText("Realizado");
                holder.tvEstado.setBackgroundResource(R.drawable.bg_realizado);
                holder.tvEstado.setTextColor(Color.WHITE);
            } else if ("Urgente".equals(expediente.getUrgencia())) {
                holder.tvEstado.setText(expediente.getUrgencia());
                holder.tvEstado.setBackgroundResource(R.drawable.bg_urgencia_urgente);
                holder.tvEstado.setTextColor(Color.WHITE);
            } else {
                holder.tvEstado.setText(expediente.getUrgencia());
                holder.tvEstado.setBackgroundResource(R.drawable.bg_urgencia);
                holder.tvEstado.setTextColor(Color.BLACK);
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
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
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvProblema = itemView.findViewById(R.id.tvProblema);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }

        public void bind(Expediente expediente) {
            tvUsuario.setText("Usuario: " + expediente.getUsuario());
            tvCliente.setText(expediente.getCliente());
            tvProblema.setText(expediente.getProblema());
            tvFecha.setText(expediente.getFecha());
            tvEstado.setText(expediente.getUrgencia());

            // Resaltar estados
            if ("Urgente".equals(expediente.getUrgencia())) {
                tvEstado.setBackgroundResource(R.drawable.bg_urgencia_urgente);
                tvEstado.setTextColor(Color.WHITE);
            } else if ("Realizado".equals(expediente.getUrgencia())) {
                tvEstado.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.verde_realizado));
                tvEstado.setTextColor(Color.WHITE);

            } else {
                tvEstado.setBackgroundResource(R.drawable.bg_urgencia);
                tvEstado.setTextColor(Color.BLACK);
            }
        }
    }
}