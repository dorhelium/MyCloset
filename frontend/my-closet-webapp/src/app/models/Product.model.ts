import {Image} from "./image.model";

export class Product {
  id: number | undefined;
  url: string | undefined;
  productName: string | undefined;
  brand: string | undefined;
  originalPrice: number | undefined;
  salePrice: number | undefined;
  onSale: boolean | undefined;
  images: Image[] | undefined;
  sizes: string[] | undefined;
  colors: string[] | undefined;



  constructor(product: any) {
    Object.assign(this, product);
  }
}
