import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {AuthenticationService} from "./authentication.service";
import {Observable} from "rxjs";

@Injectable()
export class JwtInterceptorService implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    let currentUser = this.authenticationService.currentUserValue;
    if (currentUser && currentUser.username && currentUser.password) {
      console.log('has user', currentUser);
      request = request.clone({
        setHeaders: {
          Authorization: `Basic `+ btoa(currentUser.username+':'+ currentUser.password)
        }
      });
    }

    return next.handle(request);
  }
}
