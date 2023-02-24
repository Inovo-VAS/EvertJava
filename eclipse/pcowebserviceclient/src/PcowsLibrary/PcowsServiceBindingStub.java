/**
 * PcowsServiceBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package PcowsLibrary;

public class PcowsServiceBindingStub extends org.apache.axis.client.Stub implements PcowsLibrary.PcowsService_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[16];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AddCallData");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___AddCallData"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___AddCallData"), PcowsLibrary.PcowsService___AddCallData.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___AddCallDataResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___AddCallDataResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___AddCallDataResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetCallData");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___GetCallData"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetCallData"), PcowsLibrary.PcowsService___GetCallData.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetCallDataResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___GetCallDataResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___GetCallDataResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetErrorMessage");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___GetErrorMessage"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetErrorMessage"), PcowsLibrary.PcowsService___GetErrorMessage.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetErrorMessageResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___GetErrorMessageResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___GetErrorMessageResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetServerTime");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___GetServerTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetServerTime"), PcowsLibrary.PcowsService___GetServerTime.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetServerTimeResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___GetServerTimeResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___GetServerTimeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("InsertOutboundRecord");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecord"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord"), PcowsLibrary.PcowsService___InsertOutboundRecord.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecordResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___InsertOutboundRecordResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecordResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("InsertOutboundRecord2");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecord2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord2"), PcowsLibrary.PcowsService___InsertOutboundRecord2.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord2Response"));
        oper.setReturnClass(PcowsLibrary.PcowsService___InsertOutboundRecord2Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecord2Response"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("InsertOutboundRecord3");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecord3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord3"), PcowsLibrary.PcowsService___InsertOutboundRecord3.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord3Response"));
        oper.setReturnClass(PcowsLibrary.PcowsService___InsertOutboundRecord3Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecord3Response"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("InsertOutboundRecord4");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecord4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord4"), PcowsLibrary.PcowsService___InsertOutboundRecord4.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord4Response"));
        oper.setReturnClass(PcowsLibrary.PcowsService___InsertOutboundRecord4Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___InsertOutboundRecord4Response"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RefreshService");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RefreshService"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshService"), PcowsLibrary.PcowsService___RefreshService.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshServiceResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___RefreshServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RefreshServiceResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ReloadOutboundQueues");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___ReloadOutboundQueues"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___ReloadOutboundQueues"), PcowsLibrary.PcowsService___ReloadOutboundQueues.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___ReloadOutboundQueuesResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___ReloadOutboundQueuesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RequestOutboundACDCall");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RequestOutboundACDCall"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall"), PcowsLibrary.PcowsService___RequestOutboundACDCall.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCallResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___RequestOutboundACDCallResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RequestOutboundACDCallResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RequestOutboundACDCall2");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RequestOutboundACDCall2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall2"), PcowsLibrary.PcowsService___RequestOutboundACDCall2.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall2Response"));
        oper.setReturnClass(PcowsLibrary.PcowsService___RequestOutboundACDCall2Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RequestOutboundACDCall2Response"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RequestOutboundACDCall3");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RequestOutboundACDCall3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall3"), PcowsLibrary.PcowsService___RequestOutboundACDCall3.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall3Response"));
        oper.setReturnClass(PcowsLibrary.PcowsService___RequestOutboundACDCall3Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RequestOutboundACDCall3Response"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Sum");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___Sum"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___Sum"), PcowsLibrary.PcowsService___Sum.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___SumResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___SumResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___SumResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UnloadOutboundRecord");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___UnloadOutboundRecord"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___UnloadOutboundRecord"), PcowsLibrary.PcowsService___UnloadOutboundRecord.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___UnloadOutboundRecordResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___UnloadOutboundRecordResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___UnloadOutboundRecordResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RefreshTracers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RefreshTracers"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshTracers"), PcowsLibrary.PcowsService___RefreshTracers.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshTracersResponse"));
        oper.setReturnClass(PcowsLibrary.PcowsService___RefreshTracersResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:PcowsLibrary", "PcowsService___RefreshTracersResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

    }

    public PcowsServiceBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PcowsServiceBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PcowsServiceBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___AddCallData");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___AddCallData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___AddCallDataResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___AddCallDataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetCallData");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___GetCallData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetCallDataResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___GetCallDataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetErrorMessage");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___GetErrorMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetErrorMessageResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___GetErrorMessageResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetServerTime");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___GetServerTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___GetServerTimeResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___GetServerTimeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecord.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord2");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecord2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord2Response");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecord2Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord3");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecord3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord3Response");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecord3Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord4");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecord4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecord4Response");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecord4Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___InsertOutboundRecordResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___InsertOutboundRecordResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshService");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RefreshService.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshServiceResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RefreshServiceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshTracers");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RefreshTracers.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RefreshTracersResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RefreshTracersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___ReloadOutboundQueues");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___ReloadOutboundQueues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___ReloadOutboundQueuesResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RequestOutboundACDCall.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall2");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RequestOutboundACDCall2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall2Response");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RequestOutboundACDCall2Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall3");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RequestOutboundACDCall3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCall3Response");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RequestOutboundACDCall3Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___RequestOutboundACDCallResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___RequestOutboundACDCallResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___Sum");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___Sum.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___SumResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___SumResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___UnloadOutboundRecord");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___UnloadOutboundRecord.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PcowsLibrary", ">PcowsService___UnloadOutboundRecordResponse");
            cachedSerQNames.add(qName);
            cls = PcowsLibrary.PcowsService___UnloadOutboundRecordResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public PcowsLibrary.PcowsService___AddCallDataResponse addCallData(PcowsLibrary.PcowsService___AddCallData parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#AddCallData");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "AddCallData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___AddCallDataResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___AddCallDataResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___AddCallDataResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___GetCallDataResponse getCallData(PcowsLibrary.PcowsService___GetCallData parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#GetCallData");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetCallData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___GetCallDataResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___GetCallDataResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___GetCallDataResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___GetErrorMessageResponse getErrorMessage(PcowsLibrary.PcowsService___GetErrorMessage parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#GetErrorMessage");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetErrorMessage"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___GetErrorMessageResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___GetErrorMessageResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___GetErrorMessageResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___GetServerTimeResponse getServerTime(PcowsLibrary.PcowsService___GetServerTime parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#GetServerTime");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetServerTime"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___GetServerTimeResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___GetServerTimeResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___GetServerTimeResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___InsertOutboundRecordResponse insertOutboundRecord(PcowsLibrary.PcowsService___InsertOutboundRecord parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#InsertOutboundRecord");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "InsertOutboundRecord"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___InsertOutboundRecordResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___InsertOutboundRecordResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___InsertOutboundRecordResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___InsertOutboundRecord2Response insertOutboundRecord2(PcowsLibrary.PcowsService___InsertOutboundRecord2 parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#InsertOutboundRecord2");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "InsertOutboundRecord2"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___InsertOutboundRecord2Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___InsertOutboundRecord2Response) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___InsertOutboundRecord2Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___InsertOutboundRecord3Response insertOutboundRecord3(PcowsLibrary.PcowsService___InsertOutboundRecord3 parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#InsertOutboundRecord3");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "InsertOutboundRecord3"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___InsertOutboundRecord3Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___InsertOutboundRecord3Response) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___InsertOutboundRecord3Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___InsertOutboundRecord4Response insertOutboundRecord4(PcowsLibrary.PcowsService___InsertOutboundRecord4 parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#InsertOutboundRecord4");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "InsertOutboundRecord4"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___InsertOutboundRecord4Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___InsertOutboundRecord4Response) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___InsertOutboundRecord4Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___RefreshServiceResponse refreshService(PcowsLibrary.PcowsService___RefreshService parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#RefreshService");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "RefreshService"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___RefreshServiceResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___RefreshServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___RefreshServiceResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse reloadOutboundQueues(PcowsLibrary.PcowsService___ReloadOutboundQueues parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#ReloadOutboundQueues");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "ReloadOutboundQueues"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___ReloadOutboundQueuesResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___RequestOutboundACDCallResponse requestOutboundACDCall(PcowsLibrary.PcowsService___RequestOutboundACDCall parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#RequestOutboundACDCall");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "RequestOutboundACDCall"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___RequestOutboundACDCallResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___RequestOutboundACDCallResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___RequestOutboundACDCallResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___RequestOutboundACDCall2Response requestOutboundACDCall2(PcowsLibrary.PcowsService___RequestOutboundACDCall2 parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#RequestOutboundACDCall2");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "RequestOutboundACDCall2"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___RequestOutboundACDCall2Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___RequestOutboundACDCall2Response) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___RequestOutboundACDCall2Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___RequestOutboundACDCall3Response requestOutboundACDCall3(PcowsLibrary.PcowsService___RequestOutboundACDCall3 parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#RequestOutboundACDCall3");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "RequestOutboundACDCall3"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___RequestOutboundACDCall3Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___RequestOutboundACDCall3Response) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___RequestOutboundACDCall3Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___SumResponse sum(PcowsLibrary.PcowsService___Sum parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#Sum");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Sum"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___SumResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___SumResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___SumResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___UnloadOutboundRecordResponse unloadOutboundRecord(PcowsLibrary.PcowsService___UnloadOutboundRecord parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#UnloadOutboundRecord");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "UnloadOutboundRecord"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___UnloadOutboundRecordResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___UnloadOutboundRecordResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___UnloadOutboundRecordResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public PcowsLibrary.PcowsService___RefreshTracersResponse refreshTracers(PcowsLibrary.PcowsService___RefreshTracers parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PcowsLibrary-PcowsService#RefreshTracers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "RefreshTracers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (PcowsLibrary.PcowsService___RefreshTracersResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (PcowsLibrary.PcowsService___RefreshTracersResponse) org.apache.axis.utils.JavaUtils.convert(_resp, PcowsLibrary.PcowsService___RefreshTracersResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
