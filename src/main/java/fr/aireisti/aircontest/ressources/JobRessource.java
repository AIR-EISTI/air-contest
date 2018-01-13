package fr.aireisti.aircontest.ressources;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import fr.aireisti.aircontest.models.JobInfo;

import java.util.HashMap;
import java.util.Map;

@Path("/job")
@Singleton
public class JobRessource {
    private static Sse sse;
    private static Map<String, SseBroadcaster> broadcasters = new HashMap<>();

    public JobRessource(@Context final Sse sse) {
        JobRessource.sse = sse;
    }

    @Path("{jobId}")
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void getJob(@PathParam("jobId") String jobId, @Context SseEventSink eventSink) {
        if (!JobRessource.broadcasters.containsKey(jobId))
            throw new NotFoundException("JobInfo doesn't exist.");
        SseBroadcaster broadcaster = JobRessource.broadcasters.get(jobId);
        if (broadcaster == null) {
            broadcaster = sse.newBroadcaster();
            JobRessource.broadcasters.put(jobId, broadcaster);
        }
        broadcaster.register(eventSink);
    }

    public static void broadcast(JobInfo jobInfo) {
        SseBroadcaster broadcaster = JobRessource.broadcasters.get(jobInfo.getUuid());
        if (broadcaster == null)
            return;

        OutboundSseEvent event = sse.newEventBuilder()
                .name(jobInfo.getMsgType())
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(JobInfo.class, jobInfo)
                .build();

        broadcaster.broadcast(event);
        broadcaster.close();
        JobRessource.broadcasters.remove(jobInfo.getUuid());
    }

    public static void addBroadcaster(String uuid) {
        SseBroadcaster broadcaster = sse == null ? null : sse.newBroadcaster();
        JobRessource.broadcasters.put(uuid, broadcaster);
    }
}
