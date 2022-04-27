package pl.edu.pwr.lab7.soap;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.soap.installment.*;


@Transactional
@Endpoint
public class InstallmentEndpoint {

    private final InstallmentService installmentService;

    private final EventService eventService;

    public InstallmentEndpoint(InstallmentService installmentService, EventService eventService) {
        this.installmentService = installmentService;
        this.eventService = eventService;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getAllInstallmentRequest")
    @ResponsePayload
    public GetAllInstallmentResponse getAll(@RequestPayload GetAllInstallmentRequest req) {
        var resp = new GetAllInstallmentResponse();
        resp.getInstallmentList().addAll(installmentService.getAll());
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getInstallmentRequest")
    @ResponsePayload
    public GetInstallmentResponse get(@RequestPayload GetInstallmentRequest req) {
        var installment = installmentService.getById(req.getId());
        installment = (Installment) Hibernate.unproxy(installment);
        var resp = new GetInstallmentResponse();
        resp.setInstallment(installment);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "addInstallmentRequest")
    @ResponsePayload
    public AddInstallmentResponse add(@RequestPayload AddInstallmentRequest req) {
        var installment = new Installment();
        installment.setInstallmentNum(req.getInstallmentNum());
        installment.setAmount(req.getAmount());
        installment.setEvent(eventService.getById(req.getEventId()));
        installment.setTime(req.getTime());
        installmentService.addInstallment(installment);
        return new AddInstallmentResponse();
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "deleteInstallmentRequest")
    @ResponsePayload
    public DeleteInstallmentResponse delete(@RequestPayload DeleteInstallmentRequest req) {
        installmentService.deleteById(req.getId());
        return new DeleteInstallmentResponse();
    }
}
