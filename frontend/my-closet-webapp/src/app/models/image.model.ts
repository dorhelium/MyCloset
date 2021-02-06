export class Image {
  id: number | undefined;
  imageData: string | undefined;
  constructor(image: any) {
    Object.assign(this, image);
  }
}
