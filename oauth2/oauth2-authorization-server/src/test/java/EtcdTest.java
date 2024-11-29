import com.google.protobuf.ByteString;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.lease.LeaseGrantResponse;
import io.etcd.jetcd.lock.LockResponse;
import io.etcd.jetcd.options.PutOption;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class EtcdTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        // create client using endpoints
        KV kvClient;
        Lease leaseClient;
        Client etcd = Client.builder().endpoints("http://localhost:2379").build();
        kvClient = etcd.getKVClient();
        leaseClient = etcd.getLeaseClient();
        Lock lockClient = etcd.getLockClient();


        ByteSequence oauth2 = ByteSequence.NAMESPACE_DELIMITER.concat(ByteString.copyFrom("oauth2",
                StandardCharsets.UTF_8));

        ByteSequence client = oauth2.concat(ByteSequence.NAMESPACE_DELIMITER).concat(ByteString.copyFrom("client",
                StandardCharsets.UTF_8));

        ByteSequence authorize = oauth2.concat(ByteSequence.NAMESPACE_DELIMITER).concat(ByteString.copyFrom("authorize",
                StandardCharsets.UTF_8));

        // put the key-initValue
        PutResponse putResponse1 = kvClient.put(oauth2, ByteSequence.EMPTY).get();
        PutResponse putResponse2 = kvClient.put(client, ByteSequence.EMPTY).get();
        PutResponse putResponse3 = kvClient.put(authorize, ByteSequence.EMPTY).get();
        LeaseGrantResponse grantResponse = leaseClient.grant(120).get();
        LockResponse lock =
                lockClient.lock(ByteSequence.from("lock", StandardCharsets.UTF_8), grantResponse.getID()).get();

        LeaseGrantResponse leaseGrantResponse = leaseClient.grant(120).get();
        PutResponse putResponse4 =
                kvClient.put(client.concat(ByteSequence.NAMESPACE_DELIMITER).concat(ByteString.copyFrom("1",
                                StandardCharsets.UTF_8)), ByteSequence.from("测试数据".getBytes()),
                        PutOption.builder().withLeaseId(leaseGrantResponse.getID()).build()).get();

        // get the CompletableFuture
//        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        // get the initValue from CompletableFuture
//        GetResponse response = getFuture.get();

        // delete the key
//        kvClient.delete(key).get();
    }
}
