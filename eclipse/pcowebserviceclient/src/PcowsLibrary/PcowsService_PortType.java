/**
 * PcowsService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public interface PcowsService_PortType extends java.rmi.Remote {

    /**
     * Adds data to a contact from its call identifier ID (CallId).
     * This information
     * will be associated with the contact throughout all of its handling
     * process.
     * This function is used, for example, to implement voice and data transfers.
     * 
     * Return value: When the returned value is <0, the data could not be
     * added to the
     * contact. If the value is 0, the data could be added.
     */
    public PcowsLibrary.PcowsService___AddCallDataResponse addCallData(PcowsLibrary.PcowsService___AddCallData parameters) throws java.rmi.RemoteException;

    /**
     * Gets the value of a variable attached to the contact from its
     * call identifier (CallId).
     * 
     * Return value: When the returned value is <0, the call data could not
     * be
     * retrieved or the variable to be retrieved does not exist. If the value
     * is 0,
     * the variable exists and the data could be retrieved.
     */
    public PcowsLibrary.PcowsService___GetCallDataResponse getCallData(PcowsLibrary.PcowsService___GetCallData parameters) throws java.rmi.RemoteException;

    /**
     * Gets the error message from its code.
     * 
     * Return value: Error message associated with the code.
     */
    public PcowsLibrary.PcowsService___GetErrorMessageResponse getErrorMessage(PcowsLibrary.PcowsService___GetErrorMessage parameters) throws java.rmi.RemoteException;

    /**
     * Gets the current date and time of the computer where Presence
     * Server
     * Web Services is installed.
     * 
     * Return value: Date and time of the computer where Presence Server
     * Web Services
     * is installed.
     */
    public PcowsLibrary.PcowsService___GetServerTimeResponse getServerTime(PcowsLibrary.PcowsService___GetServerTime parameters) throws java.rmi.RemoteException;

    /**
     * Deprecated. Refer to InsertOutboundRecord4.
     * 
     * Inserts a new outbound record into a service.
     * 
     * Return value: When the returned value is <0, a new record could not
     * be inserted
     * into an outbound service. If the value is 0, the record could be successfully
     * inserted.
     */
    public PcowsLibrary.PcowsService___InsertOutboundRecordResponse insertOutboundRecord(PcowsLibrary.PcowsService___InsertOutboundRecord parameters) throws java.rmi.RemoteException;

    /**
     * Deprecated. Refer to InsertOutboundRecord4.
     * 
     * Inserts a new outbound record into a service.
     * 
     * Return value: When the returned value is <0, a new record could not
     * be inserted
     * into an outbound service. If the value is 0, the record could be successfully
     * inserted.
     */
    public PcowsLibrary.PcowsService___InsertOutboundRecord2Response insertOutboundRecord2(PcowsLibrary.PcowsService___InsertOutboundRecord2 parameters) throws java.rmi.RemoteException;

    /**
     * Deprecated. Refer to InsertOutboundRecord4.
     * 
     * Inserts a new outbound record into a service.
     * 
     * Return value: When the returned value is <0, a new record could not
     * be inserted
     * into an outbound service. If the value is 0, the record could be successfully
     * inserted.
     */
    public PcowsLibrary.PcowsService___InsertOutboundRecord3Response insertOutboundRecord3(PcowsLibrary.PcowsService___InsertOutboundRecord3 parameters) throws java.rmi.RemoteException;

    /**
     * Inserts a new outbound record into a service.
     * 
     * Return value: When the returned value is <0, a new record could not
     * be inserted
     * into an outbound service. If the value is 0, the record could be successfully
     * inserted.
     */
    public PcowsLibrary.PcowsService___InsertOutboundRecord4Response insertOutboundRecord4(PcowsLibrary.PcowsService___InsertOutboundRecord4 parameters) throws java.rmi.RemoteException;

    /**
     * Refreshes in Presence Server all the information about an enabled
     * service using
     * the current information found in the database.
     * 
     * Return value: When the returned value is <0, the service could not
     * be refreshed.
     * If the value is 0, the service has been successfully refreshed.
     */
    public PcowsLibrary.PcowsService___RefreshServiceResponse refreshService(PcowsLibrary.PcowsService___RefreshService parameters) throws java.rmi.RemoteException;

    /**
     * Reloads the buffers of an outbound service that Presence Server
     * manages in
     * memory.
     * 
     * Return value: When the returned value is <0, the buffers of the outbound
     * service
     * could not be reloaded. If the value is 0, the buffers of the outbound
     * service
     * have been successfully reloaded.
     */
    public PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse reloadOutboundQueues(PcowsLibrary.PcowsService___ReloadOutboundQueues parameters) throws java.rmi.RemoteException;

    /**
     * Deprecated. Refer to RequestOutboundACDCall3.
     * 
     * Requests a new manual outbound ACD contact for an agent in a particular
     * service.
     * 
     * Return value: When the returned value is <0, the new manual outbound
     * ACD contact
     * could not be requested for the agent. If the value is 0, the new manual
     * outbound ACD contact has been successfully requested.
     */
    public PcowsLibrary.PcowsService___RequestOutboundACDCallResponse requestOutboundACDCall(PcowsLibrary.PcowsService___RequestOutboundACDCall parameters) throws java.rmi.RemoteException;

    /**
     * Deprecated. Refer to RequestOutboundACDCall3.
     * 
     * Requests a new manual outbound ACD contact for an agent in a particular
     * service.
     * 
     * Return value: When the returned value is <0, the new manual outbound
     * ACD contact
     * could not be requested for the agent. If the value is 0, the new manual
     * outbound ACD contact has been successfully requested.
     */
    public PcowsLibrary.PcowsService___RequestOutboundACDCall2Response requestOutboundACDCall2(PcowsLibrary.PcowsService___RequestOutboundACDCall2 parameters) throws java.rmi.RemoteException;

    /**
     * Requests a new manual outbound ACD contact for an agent in
     * a particular service.
     * 
     * Return value: When the returned value is <0, the new manual outbound
     * ACD contact
     * could not be requested for the agent. If the value is 0, the new manual
     * outbound ACD contact has been successfully requested.
     */
    public PcowsLibrary.PcowsService___RequestOutboundACDCall3Response requestOutboundACDCall3(PcowsLibrary.PcowsService___RequestOutboundACDCall3 parameters) throws java.rmi.RemoteException;

    /**
     * Adds two integer values and returns the result of the sum.
     * This method is commonly used to verify that Presence Server Web Services
     * has
     * been properly installed.
     * 
     * Return value: Returns the result of adding the two values.
     */
    public PcowsLibrary.PcowsService___SumResponse sum(PcowsLibrary.PcowsService___Sum parameters) throws java.rmi.RemoteException;

    /**
     * Unloads an outbound record from a service.
     * 
     * Return value: When the returned value is <0, the record could not
     * be unloaded.
     * If the value is >0, the number of outbound records that have been
     * unloaded is
     * indicated.
     */
    public PcowsLibrary.PcowsService___UnloadOutboundRecordResponse unloadOutboundRecord(PcowsLibrary.PcowsService___UnloadOutboundRecord parameters) throws java.rmi.RemoteException;

    /**
     * Refreshes the configuration values of the tracing objects.
     */
    public PcowsLibrary.PcowsService___RefreshTracersResponse refreshTracers(PcowsLibrary.PcowsService___RefreshTracers parameters) throws java.rmi.RemoteException;
}
