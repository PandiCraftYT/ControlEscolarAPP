package com.example.controlescolar.api;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface ApiInterface {

    @POST("api/android/authenticate")
    Call<ApiResponse> authenticate(@Body JsonObject body);

    @POST("api/android/getkardex")
    Call<KardexResponse> getKardex(@Body JsonObject body);

    @POST("api/android/solicitudconstancia")
    Call<SolicitudConstanciaResponse> solicitudConstancia(@Body JsonObject body);

    @POST("api/android/solicitudjustificante")
    Call<SolicitudJustificanteResponse> solicitudJustificante(@Body JsonObject body);

    @POST("api/android/guardarsolicitudconstanciaandroid")
    Call<GuardarSolicitudConstanciaAndroidResponse> guardarSolicitudConstanciaAndroid(@Body JsonObject body);

    @POST("api/android/guardarsolicitudjustificanteandroid")
    Call<GuardarSolicitudJustificanteAndroidResponse> guardarSolicitudJustificanteAndroid(@Body JsonObject body);

    @POST("api/android/generarconstanciaandroid")
    @Streaming
    Call<ResponseBody> generarConstanciaAndroid(@Body JsonObject body);

    @POST("api/android/generarjustificanteandroid")
    @Streaming
    Call<ResponseBody> generarJustificanteAndroid(@Body JsonObject body);


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


    public class KardexResponse {
        private boolean success;
        private String message;
        private KardexData data;

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public KardexData getData() {
            return data;
        }
    }

    public class KardexData {
        private List<KardexEntry> curriculo;

        public List<KardexEntry> getCurriculo() {
            return curriculo;
        }
    }

    public class KardexEntry {
        private String materia;
        private String calificacion;
        private String semestre;

        public String getMateria() {
            return materia;
        }

        public String getCalificacion() {
            return calificacion;
        }

        public String getSemestre() {
            return semestre;
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


    public class SolicitudJustificanteResponse {
        private String status;
        private SolicitudJustificanteData data;

        public String getStatus() {
            return status;
        }

        public SolicitudJustificanteData getData() {
            return data;
        }
    }

    public class SolicitudJustificanteData {
        private String user;
        private List<Justificante> justificantesSolicitados;

        public String getUser() {
            return user;
        }

        public List<Justificante> getJustificantesSolicitados() {
            return justificantesSolicitados;
        }
    }

    public class Justificante {
        private String folio;
        private String fecha_solicitud;
        private String fecha_justificar;
        private String motivo;
        private String descripcion_motivo;
        private String estado;
        private String no_cuenta;
        private String archivo_nombre;
        private String archivo_data;

        public String getFolio() {
            return folio;
        }

        public String getFechaSolicitud() {
            return fecha_solicitud;
        }

        public String getFechaJustificar() {
            return fecha_justificar;
        }

        public String getMotivo() {
            return motivo;
        }

        public String getDescripcionMotivo() {
            return descripcion_motivo;
        }

        public String getEstado() {
            return estado;
        }

        public String getNoCuenta() {
            return no_cuenta;
        }

        public String getArchivoNombre() {
            return archivo_nombre;
        }

        public String getArchivoData() {
            return archivo_data;
        }
    }

    public class GuardarSolicitudConstanciaAndroidResponse {
        private String status;
        private String message;

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    public class GuardarSolicitudJustificanteAndroidResponse {
        private String status;
        private String message;

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

}
