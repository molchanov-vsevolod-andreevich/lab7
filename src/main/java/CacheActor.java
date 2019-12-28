import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CacheActor extends AbstractActor {
    private List<String> serversList;

    static Props props() {
        return Props.create(CacheActor.class);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Servers.class, req -> {
                    System.out.println(ZookeeperAppConstants.WATCHER_MESSAGE);
                    serversList = req.getServersList();
                })
                .match(CacheActor.GetRandomServer.class, msg -> {
                    int randServerIdx = new Random().nextInt(serversList.size());
                    String randServer = serversList.get(randServerIdx);
                    System.out.println(ZookeeperAppConstants.REDIRECT_MESSAGE + randServer);
                    sender().tell(randServer, self());
                })
                .build();
    }

    static class GetRandomServer {
    }

}
