package com.xyl.game.common;

//
//@Configuration
//public class WebServerConfiguration
//{
//    @Bean
//    public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory()
//    {
//        TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
//        tomcatFactory.setPort(80);
//        tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
//        tomcatFactory.addContextCustomizers(new MyTomcatContextCustomizer());
//        return tomcatFactory;
//    }
//}
//class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer
//{
//    @Override
//    public void customize(Connector connector)
//    {
//        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
//        //设置最大连接数
//        protocol.setMaxConnections(2000);
//        //设置最大线程数
//        protocol.setMaxThreads(2000);
//        protocol.setConnectionTimeout(30000);
//    }
//}
//
//class MyTomcatContextCustomizer implements TomcatContextCustomizer{
//
//    @Override
//    public void customize(Context context) {
//        context.addParameter("org.apache.tomcat.websocket.executorCoreSize","500");
//        context.addParameter("org.apache.tomcat.websocket.executorMaxSize","2000");
//        context.addParameter("org.apache.tomcat.websocket.executorKeepAliveTimeSeconds","150");
//
//    }
//}