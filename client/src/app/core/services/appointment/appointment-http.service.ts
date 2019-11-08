import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Appointment } from '../../models/appointment';
import { environment } from '../../../../environments/environment';

@Injectable()
export class AppointmentHttpService {


  api = `${environment.api}/appointments`;

  constructor(private httpClient: HttpClient) { }

  public find(id: number) {
    return this.httpClient.get<Appointment[]>(`${this.api}/${id}`);
  }

  public findByClient(clientDocumentNumber: string) {
    return this.httpClient.get<Appointment[]>(`${this.api}?clientDocumentNumber${clientDocumentNumber}`);
  }

  public findAll() {
    return this.httpClient.get<Appointment[]>(this.api);
  }

  public create(appointment: Appointment) {
    return this.httpClient.post(this.api, appointment);
  }

  // public update(appointment: Appointment) {}

  public delete(id: number) {
    return this.httpClient.delete(`${this.api}/${id}`);
  }
}