import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material';

import { Client } from '@app/core/models/client';
import { AppointmentHttpService } from '@app/core/services/appointment/appointment-http.service';
import { ClientHttpService } from '@app/core/services/client/client-http.service';
import { Appointment } from '@app/core/models/appointment';
import { DateUtils } from '@app/shared/utils/date-utils';

@Component({
  selector: 'app-new-appointment',
  templateUrl: './new-appointment.component.html',
  styleUrls: ['./new-appointment.component.sass']
})
export class NewAppointmentComponent implements OnInit {

  appointmentForm: FormGroup;
  minDate: Date;
  options: Client[];
  filteredOptions: Observable<Client[]>;

  constructor(private router: Router, private route: ActivatedRoute,
              private appointmentService: AppointmentHttpService,
              private clientService: ClientHttpService,
              private formBuilder: FormBuilder,
              private snackbar: MatSnackBar) {
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate() + 1);
  }

  ngOnInit() {
    this._initForm();
    this._getClients();
  }

  save() {
    const client = this.options.find(clientT => {
      return clientT.fullName === this.appointmentForm.value.client;
    });
    const appointment = new Appointment();
    appointment.client = client;
    appointment.appointmentDate = new Date(this.appointmentForm.value.appointmentDate);
    const [horas, minutos] = DateUtils.convert12hto24h(this.appointmentForm.value.appointmentTime);
    appointment.appointmentDate.setHours(horas - 5, minutos, 0);
    this.appointmentService.create(appointment).subscribe(() => {
      this.snackbar.open('La cita se registró correctamente', 'Cerrar', {
        duration: 3000,
      });
      this.router.navigate(['/appointments']);
    }, this._handleError);
  }

  private _filter(value: string): Client[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option =>
      option.fullName.toLowerCase().indexOf(filterValue) === 0
        || option.documentNumber.indexOf(filterValue) === 0);
  }

  private _initForm() {
    this.appointmentForm = this.formBuilder.group({
      client: ['', Validators.required],
      appointmentDate: [null, Validators.required],
      appointmentTime: [null, Validators.required]
    });
  }

  private _getClients() {
    this.clientService.findAll().subscribe(clients => {
      this.options = clients;
      this.route.queryParams.subscribe(params => {
        const clientDocumentNumber = params.clientDocumentNumber;
        if (clientDocumentNumber) {
          this.appointmentForm.get('client').setValue(this.options.find(client => {
            return client.documentNumber === clientDocumentNumber;
          }).fullName);
        }
      });
      this.filteredOptions = this.appointmentForm.get('client').valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value))
      );
    });
  }

  private _handleError(error: any) {
    let message = 'Ocurrió un error almacenando la cita';
    if (error.error.statusCode) {
      message = error.error.errorMessage;
    }
    this.snackbar.open(message, 'Cerrar', {
      duration: 10000,
    });
  }
}
