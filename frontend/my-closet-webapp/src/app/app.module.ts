import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainPageComponent } from './main-page/main-page.component';
import { FooterComponent } from './footer/footer.component';
import { HomepageComponent } from './homepage/homepage.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule, HttpClient, HTTP_INTERCEPTORS} from "@angular/common/http";
import { UserPageComponent } from './user-page/user-page.component';
import {JwtInterceptorService} from "./Service/jwt-interceptor.service";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    FooterComponent,
    HomepageComponent,
    UserPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule
  ],
  providers: [
   {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorService, multi:true}
  ],

  bootstrap: [AppComponent],
  exports: [
    HttpClientModule
  ]
})
export class AppModule { }
