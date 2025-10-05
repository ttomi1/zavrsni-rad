import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'fullImageUrl',
  standalone: false
})
export class FullImageUrlPipe implements PipeTransform {

  transform(url?: string): string {

    if (!url) {
      return 'assets/pfp1.jpg';
    }

    if (url.startsWith('http')) {
      return url;
    }

    return 'http://localhost:8080' + url;
  }

}
