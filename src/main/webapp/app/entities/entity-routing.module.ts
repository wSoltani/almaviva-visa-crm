import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'visa-documents',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.visaDocuments.home.title' },
        loadChildren: () => import('./visa-documents/visa-documents.module').then(m => m.VisaDocumentsModule),
      },
      {
        path: 'visa',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.visa.home.title' },
        loadChildren: () => import('./visa/visa.module').then(m => m.VisaModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'phone-activation',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.phoneActivation.home.title' },
        loadChildren: () => import('./phone-activation/phone-activation.module').then(m => m.PhoneActivationModule),
      },
      {
        path: 'email-activation',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.emailActivation.home.title' },
        loadChildren: () => import('./email-activation/email-activation.module').then(m => m.EmailActivationModule),
      },
      {
        path: 'appointment',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.appointment.home.title' },
        loadChildren: () => import('./appointment/appointment.module').then(m => m.AppointmentModule),
      },
      {
        path: 'site',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.site.home.title' },
        loadChildren: () => import('./site/site.module').then(m => m.SiteModule),
      },
      {
        path: 'site-configuration',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.siteConfiguration.home.title' },
        loadChildren: () => import('./site-configuration/site-configuration.module').then(m => m.SiteConfigurationModule),
      },
      {
        path: 'day-off',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.dayOff.home.title' },
        loadChildren: () => import('./day-off/day-off.module').then(m => m.DayOffModule),
      },
      {
        path: 'folder',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.folder.home.title' },
        loadChildren: () => import('./folder/folder.module').then(m => m.FolderModule),
      },
      {
        path: 'mandate',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.mandate.home.title' },
        loadChildren: () => import('./mandate/mandate.module').then(m => m.MandateModule),
      },
      {
        path: 'person',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.person.home.title' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'av-service',
        data: { pageTitle: 'almavivaVisaServiceCrmApp.aVService.home.title' },
        loadChildren: () => import('./av-service/av-service.module').then(m => m.AVServiceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
