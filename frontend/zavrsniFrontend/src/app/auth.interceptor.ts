import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpInterceptorFn,
  HttpRequest
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {AuthService} from './services/auth.service';
import {jwtDecode} from 'jwt-decode';

@Injectable()
export class authInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    if (token) {
      const decoded: any = jwtDecode(token);
      const currentTime = Date.now() / 1000;
      if (decoded.exp < currentTime && !this.isRefreshing) {
        console.log('Token expired, refreshing...');

        this.isRefreshing = true;
        return this.authService.refreshToken().pipe(
          switchMap((response: any) => {
            this.isRefreshing = false;

            if (response?.accessToken) {
              localStorage.setItem('token', response.accessToken);

              const clonedReq = req.clone({
                setHeaders: {
                  Authorization: `Bearer ${response.accessToken}`
                }
              });

              return next.handle(clonedReq);
            }

            return this.logoutUser();
          }),
          catchError((error: HttpErrorResponse) => {
            console.error('Refresh token failed:', error);
            this.isRefreshing = false;
            return this.logoutUser();
          })
        );
      }
      const clonedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(clonedReq);
    }
    return next.handle(req);
  }

  private logoutUser(): Observable<HttpEvent<any>> {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
    return throwError(() => new Error('Session expired, user logged out.'));
  }
}
