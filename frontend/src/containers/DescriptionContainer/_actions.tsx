import { GetDescriptionPage } from '../../generated/GetDescriptionPage';
import { ColumnItem } from '../../components/column/_types';

export const parseBreadcrumbsData = (data?: GetDescriptionPage): ColumnItem[] =>
	[...(data?.categoryBreadcrumbsWithUrl ?? [])]
		.reverse()
		.map((breadcrumb, index, arr) => ({
			...breadcrumb,
			active: !!(index === arr.length - 1),
		}));

export const parseData = (
	key: 'children' | 'neighbors',
	data?: GetDescriptionPage,
): ColumnItem[] =>
	(data?.singleCategoryWithUrl[key] ?? []).map(category => ({
		...category,
		active: !!(category.id === data?.singleCategoryWithUrl.id),
	}));
