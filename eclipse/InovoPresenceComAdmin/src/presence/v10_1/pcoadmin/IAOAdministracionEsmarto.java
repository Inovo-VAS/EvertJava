package presence.v10_1.pcoadmin  ;

import com4j.*;

/**
 * Dispatch interface for AOAdministracionEsmarto Object
 */
@IID("{14783D72-50C3-11D4-BD18-000024C8DB7D}")
public interface IAOAdministracionEsmarto extends Com4jObject {
  // Methods:
  /**
   * @param codcam Mandatory int parameter.
   * @param codcarga Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(7)
  boolean activarCarga(
    int codcam,
    int codcarga);


  /**
   * @param nombre Mandatory java.lang.String parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(8)
  boolean conexionServidor(
    java.lang.String nombre);


  /**
   * @param codcam Mandatory int parameter.
   * @param codcarga Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(9)
  boolean desactivarCarga(
    int codcam,
    int codcarga);


  /**
   * @param campoCampana Mandatory java.lang.String parameter.
   * @param nombreDatabase Mandatory java.lang.String parameter.
   * @param username Mandatory java.lang.String parameter.
   * @param password Mandatory java.lang.String parameter.
   * @param tabla Mandatory java.lang.String parameter.
   * @param idFuente Mandatory java.lang.String parameter.
   * @param codigoCarga Mandatory int parameter.
   * @param descripcion Mandatory java.lang.String parameter.
   * @param prioridad Mandatory java.lang.String parameter.
   * @param nombre Mandatory java.lang.String parameter.
   * @param telefono Mandatory java.lang.String parameter.
   * @param programado Mandatory java.lang.String parameter.
   * @param capturado Mandatory java.lang.String parameter.
   * @param telefono2 Mandatory java.lang.String parameter.
   * @param telefono3 Mandatory java.lang.String parameter.
   * @param descTelf Mandatory java.lang.String parameter.
   * @param descTelf2 Mandatory java.lang.String parameter.
   * @param descTelf3 Mandatory java.lang.String parameter.
   * @param filtro Mandatory java.lang.String parameter.
   * @param errores Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(10)
  boolean cargaMulticampana(
    java.lang.String campoCampana,
    java.lang.String nombreDatabase,
    java.lang.String username,
    java.lang.String password,
    java.lang.String tabla,
    java.lang.String idFuente,
    int codigoCarga,
    java.lang.String descripcion,
    java.lang.String prioridad,
    java.lang.String nombre,
    java.lang.String telefono,
    java.lang.String programado,
    java.lang.String capturado,
    java.lang.String telefono2,
    java.lang.String telefono3,
    java.lang.String descTelf,
    java.lang.String descTelf2,
    java.lang.String descTelf3,
    java.lang.String filtro,
    Holder<java.lang.String> errores);


  /**
   * @param listaServidores Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(5) //= 0x5. The runtime will prefer the VTID if present
  @VTID(11)
  boolean obtenerServidoresActivos(
    Holder<java.lang.String> listaServidores);


  /**
   * @param codcam Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(7) //= 0x7. The runtime will prefer the VTID if present
  @VTID(12)
  boolean pausarServicio(
    int codcam);


  /**
   * @param codcam Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(8) //= 0x8. The runtime will prefer the VTID if present
  @VTID(13)
  boolean reanudarServicio(
    int codcam);


  /**
   * @param codcam Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(9) //= 0x9. The runtime will prefer the VTID if present
  @VTID(14)
  boolean recargarColasServicio(
    int codcam);


  /**
   * @param nombreDatabase Mandatory java.lang.String parameter.
   * @param username Mandatory java.lang.String parameter.
   * @param password Mandatory java.lang.String parameter.
   * @param tabla Mandatory java.lang.String parameter.
   * @param codigoServicio Mandatory int parameter.
   * @param idFuente Mandatory java.lang.String parameter.
   * @param codigoCarga Mandatory int parameter.
   * @param prioridad Mandatory java.lang.String parameter.
   * @param nombre Mandatory java.lang.String parameter.
   * @param telefono Mandatory java.lang.String parameter.
   * @param programado Mandatory java.lang.String parameter.
   * @param capturado Mandatory java.lang.String parameter.
   * @param telefono2 Mandatory java.lang.String parameter.
   * @param telefono3 Mandatory java.lang.String parameter.
   * @param descTelf Mandatory java.lang.String parameter.
   * @param descTelf2 Mandatory java.lang.String parameter.
   * @param descTelf3 Mandatory java.lang.String parameter.
   * @param filtro Mandatory java.lang.String parameter.
   * @param errores Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(10) //= 0xa. The runtime will prefer the VTID if present
  @VTID(15)
  boolean actualizacionCarga(
    java.lang.String nombreDatabase,
    java.lang.String username,
    java.lang.String password,
    java.lang.String tabla,
    int codigoServicio,
    java.lang.String idFuente,
    int codigoCarga,
    java.lang.String prioridad,
    java.lang.String nombre,
    java.lang.String telefono,
    java.lang.String programado,
    java.lang.String capturado,
    java.lang.String telefono2,
    java.lang.String telefono3,
    java.lang.String descTelf,
    java.lang.String descTelf2,
    java.lang.String descTelf3,
    java.lang.String filtro,
    Holder<java.lang.String> errores);


  /**
   * @param codcam Mandatory int parameter.
   * @param codcarga Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(11) //= 0xb. The runtime will prefer the VTID if present
  @VTID(16)
  boolean cargaActivada(
    int codcam,
    int codcarga);


  /**
   * @param tipoDatabase Mandatory int parameter.
   * @param campoCampana Mandatory java.lang.String parameter.
   * @param nombreDatabase Mandatory java.lang.String parameter.
   * @param username Mandatory java.lang.String parameter.
   * @param password Mandatory java.lang.String parameter.
   * @param tabla Mandatory java.lang.String parameter.
   * @param idFuente Mandatory java.lang.String parameter.
   * @param codigoCarga Mandatory int parameter.
   * @param descripcion Mandatory java.lang.String parameter.
   * @param prioridad Mandatory java.lang.String parameter.
   * @param nombre Mandatory java.lang.String parameter.
   * @param telefono Mandatory java.lang.String parameter.
   * @param programado Mandatory java.lang.String parameter.
   * @param capturado Mandatory java.lang.String parameter.
   * @param telefono2 Mandatory java.lang.String parameter.
   * @param telefono3 Mandatory java.lang.String parameter.
   * @param descTelf Mandatory int parameter.
   * @param descTelf2 Mandatory int parameter.
   * @param descTelf3 Mandatory int parameter.
   * @param filtro Mandatory java.lang.String parameter.
   * @param errores Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(12) //= 0xc. The runtime will prefer the VTID if present
  @VTID(17)
  boolean cargaMulticampana2(
    int tipoDatabase,
    java.lang.String campoCampana,
    java.lang.String nombreDatabase,
    java.lang.String username,
    java.lang.String password,
    java.lang.String tabla,
    java.lang.String idFuente,
    int codigoCarga,
    java.lang.String descripcion,
    java.lang.String prioridad,
    java.lang.String nombre,
    java.lang.String telefono,
    java.lang.String programado,
    java.lang.String capturado,
    java.lang.String telefono2,
    java.lang.String telefono3,
    int descTelf,
    int descTelf2,
    int descTelf3,
    java.lang.String filtro,
    Holder<java.lang.String> errores);


  /**
   * @param tipoDatabase Mandatory int parameter.
   * @param nombreDatabase Mandatory java.lang.String parameter.
   * @param username Mandatory java.lang.String parameter.
   * @param password Mandatory java.lang.String parameter.
   * @param tabla Mandatory java.lang.String parameter.
   * @param codigoServicio Mandatory int parameter.
   * @param idFuente Mandatory java.lang.String parameter.
   * @param codigoCarga Mandatory int parameter.
   * @param prioridad Mandatory java.lang.String parameter.
   * @param nombre Mandatory java.lang.String parameter.
   * @param telefono Mandatory java.lang.String parameter.
   * @param programado Mandatory java.lang.String parameter.
   * @param capturado Mandatory java.lang.String parameter.
   * @param telefono2 Mandatory java.lang.String parameter.
   * @param telefono3 Mandatory java.lang.String parameter.
   * @param descTelf Mandatory int parameter.
   * @param descTelf2 Mandatory int parameter.
   * @param descTelf3 Mandatory int parameter.
   * @param filtro Mandatory java.lang.String parameter.
   * @param anadir Mandatory boolean parameter.
   * @param eliminar Mandatory boolean parameter.
   * @param modificar Mandatory boolean parameter.
   * @param errores Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(13) //= 0xd. The runtime will prefer the VTID if present
  @VTID(18)
  boolean actualizacionCarga2(
    int tipoDatabase,
    java.lang.String nombreDatabase,
    java.lang.String username,
    java.lang.String password,
    java.lang.String tabla,
    int codigoServicio,
    java.lang.String idFuente,
    int codigoCarga,
    java.lang.String prioridad,
    java.lang.String nombre,
    java.lang.String telefono,
    java.lang.String programado,
    java.lang.String capturado,
    java.lang.String telefono2,
    java.lang.String telefono3,
    int descTelf,
    int descTelf2,
    int descTelf3,
    java.lang.String filtro,
    boolean anadir,
    boolean eliminar,
    boolean modificar,
    Holder<java.lang.String> errores);


  // Properties:
}
