import com.google.protobuf.Any;
import io.envoyproxy.envoy.config.core.v3.Address;
import io.envoyproxy.envoy.config.core.v3.SocketAddress;
import io.envoyproxy.envoy.config.endpoint.v3.ClusterLoadAssignment;
import io.envoyproxy.envoy.config.endpoint.v3.Endpoint;
import io.envoyproxy.envoy.config.endpoint.v3.LbEndpoint;
import io.envoyproxy.envoy.config.endpoint.v3.LocalityLbEndpoints;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryResponse;
import io.envoyproxy.envoy.service.endpoint.v3.EndpointDiscoveryServiceGrpc;
import io.grpc.stub.StreamObserver;

public class EndpointDiscoveryServiceGrpcImpl extends EndpointDiscoveryServiceGrpc.EndpointDiscoveryServiceImplBase {
    @Override
    public StreamObserver<DiscoveryRequest> streamEndpoints(StreamObserver<DiscoveryResponse> responseObserver) {
        System.out.println("run grpc ...");

        StreamObserver<DiscoveryRequest> so = new StreamObserver<>() {

            @Override
            public void onNext(DiscoveryRequest request) {
                //接收客户端每一次发送的数据，返回给客户端
                //showRequest(request);

                SocketAddress sa1 = SocketAddress.newBuilder().setAddress("10.244.0.214").setPortValue(5588).build();
                SocketAddress sa2 = SocketAddress.newBuilder().setAddress("10.244.0.201").setPortValue(5588).build();
                Address address1 = Address.newBuilder().setSocketAddress(sa1).build();
                Address address2 = Address.newBuilder().setSocketAddress(sa2).build();
                Endpoint end1 = Endpoint.newBuilder().setAddress(address1).build();
                Endpoint end2 = Endpoint.newBuilder().setAddress(address2).build();
                LbEndpoint lb1 = LbEndpoint.newBuilder().setEndpoint(end1).build();
                LbEndpoint lb2 = LbEndpoint.newBuilder().setEndpoint(end2).build();
                LocalityLbEndpoints llb = LocalityLbEndpoints.newBuilder().addLbEndpoints(lb1).addLbEndpoints(lb2).build();
                ClusterLoadAssignment cla = ClusterLoadAssignment.newBuilder()
                        /*
                         * 这里配置的ClusterName 应该是路由对应的cluster name 而不是 node中的cluster
                         * route: { cluster: user-service }
                         */
                        .setClusterName("user-service")
                        .addEndpoints(llb).build();
                final DiscoveryResponse dr = DiscoveryResponse.newBuilder().setVersionInfo("1.0.0")
                        .setTypeUrl("type.googleapis.com/envoy.config.endpoint.v3.ClusterLoadAssignment")
                        .addResources(Any.pack(cla))
                        .build();

                /*
                 * 客户端模式这里不会去关闭StreamObserver
                 * 即不会调用 responseObserver.onCompleted();方法
                 */
                responseObserver.onNext(dr);
                System.out.println("send DiscoveryResponse ");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError");
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                //当客户端数据发送完毕后调用此方法，返回客户端
                SocketAddress sa1 = SocketAddress.newBuilder().setAddress("10.244.0.214").setPortValue(5588).build();
                SocketAddress sa2 = SocketAddress.newBuilder().setAddress("10.244.0.201").setPortValue(5588).build();
                Address address1 = Address.newBuilder().setSocketAddress(sa1).build();
                Address address2 = Address.newBuilder().setSocketAddress(sa2).build();
                Endpoint end1 = Endpoint.newBuilder().setAddress(address1).build();
                Endpoint end2 = Endpoint.newBuilder().setAddress(address2).build();
                LbEndpoint lb1 = LbEndpoint.newBuilder().setEndpoint(end1).build();
                LbEndpoint lb2 = LbEndpoint.newBuilder().setEndpoint(end2).build();
                LocalityLbEndpoints llb = LocalityLbEndpoints.newBuilder().addLbEndpoints(lb1).addLbEndpoints(lb2).build();
                ClusterLoadAssignment cla = ClusterLoadAssignment.newBuilder()
                        .setClusterName("user-service")
                        .addEndpoints(llb).build();
                final DiscoveryResponse dr = DiscoveryResponse.newBuilder().setVersionInfo("1.0.0")
                        .addResources(Any.pack(cla))
                        .build();

                System.out.println("onCompleted");
                responseObserver.onNext(dr);
                responseObserver.onCompleted();
            }

        };
        return so;
    }
}
