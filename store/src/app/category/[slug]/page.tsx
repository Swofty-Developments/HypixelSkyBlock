import { notFound } from "next/navigation";
import CategoryPage from "@/components/store/CategoryPage";
import { productCategories } from "@/data/store";

export default async function StoreCategory({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = await params;
  const category = productCategories[slug];

  if (!category) {
    notFound();
  }

  return <CategoryPage category={category} />;
}
