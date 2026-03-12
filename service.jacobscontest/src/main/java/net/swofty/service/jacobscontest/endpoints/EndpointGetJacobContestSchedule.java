package net.swofty.service.jacobscontest.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.jacobscontest.GetJacobContestScheduleProtocol;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.jacobscontest.JacobsContestScheduler;

import java.util.List;

public class EndpointGetJacobContestSchedule implements ServiceEndpoint<
    GetJacobContestScheduleProtocol.GetJacobContestScheduleMessage,
    GetJacobContestScheduleProtocol.GetJacobContestScheduleResponse> {

    @Override
    public ProtocolObject<
        GetJacobContestScheduleProtocol.GetJacobContestScheduleMessage,
        GetJacobContestScheduleProtocol.GetJacobContestScheduleResponse> associatedProtocolObject() {
        return new GetJacobContestScheduleProtocol();
    }

    @Override
    public GetJacobContestScheduleProtocol.GetJacobContestScheduleResponse onMessage(
        ServiceProxyRequest message,
        GetJacobContestScheduleProtocol.GetJacobContestScheduleMessage body
    ) {
        int year = JacobsContestScheduler.getYear(body.calendarElapsed());
        List<GetJacobContestScheduleProtocol.ContestScheduleEntry> schedule = JacobsContestScheduler.generateYear(year);
        return new GetJacobContestScheduleProtocol.GetJacobContestScheduleResponse(
            year,
            JacobsContestScheduler.getActiveIndex(body.calendarElapsed(), schedule),
            JacobsContestScheduler.getUpcoming(body.calendarElapsed(), Math.max(1, body.upcomingCount()))
        );
    }
}
