package com.example.controlescolar.api;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/android/authenticate")
    Call<ApiResponse> authenticate(@Body JsonObject body);

    @POST("api/android/solicitudconstancia")
    Call<SolicitudConstanciaResponse> solicitudConstancia(@Body JsonObject body);

    public class ApiResponse {
        private boolean success;
        private String message;
        private Data data;

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Data getData() {
            return data;
        }

        public class Data {
            private User user;

            public User getUser() {
                return user;
            }
        }

        public class User {
            private String no_cuenta;
            private String nombre;
            private String unidad_academica_id;
            private String carrera_id;
            private String plan_estudio_id;
            private String grupo_id;
            private String semestre_id;

            // Otros campos que devuelva tu API

            public String getNoCuenta() {
                return no_cuenta;
            }

            public String getNombre() {
                return nombre;
            }

            public String getUnidadAcademicaId() {
                return unidad_academica_id;
            }

            public String getCarreraId() {
                return carrera_id;
            }

            public String getPlanEstudioId() {
                return plan_estudio_id;
            }

            public String getGrupoId() {
                return grupo_id;
            }

            public String getSemestreId() {
                return semestre_id;
            }
        }
    }

    public class SolicitudConstanciaResponse {
        private String status;
        private SolicitudConstanciaData data;

        public String getStatus() {
            return status;
        }

        public SolicitudConstanciaData getData() {
            return data;
        }
    }

    public class SolicitudConstanciaData {
        private String user;
        private List<Constancia> constanciasSolicitadas;

        public String getUser() {
            return user;
        }

        public List<Constancia> getConstanciasSolicitadas() {
            return constanciasSolicitadas;
        }
    }

    public class Constancia {
        private String folio;
        private String no_cuenta;
        private String tipo_constancia;
        private String estado;
        private String fecha_solicitud;

        public String getFolio() {
            return folio;
        }

        public String getNoCuenta() {
            return no_cuenta;
        }

        public String getTipoConstancia() {
            return tipo_constancia;
        }

        public String getEstado() {
            return estado;
        }

        public String getFechaSolicitud() {
            return fecha_solicitud;
        }
    }
}
