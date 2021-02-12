package xyz.deltacare.empresa.event;

import xyz.deltacare.empresa.domain.Beneficiario;

public interface EventSender {
    void send(Beneficiario beneficiario);
}
