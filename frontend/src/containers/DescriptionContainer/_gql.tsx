import gql from 'graphql-tag';

export const GET_DESCRIPTION_PAGE = gql`
	query GetDescriptionPage($url: String!, $language: Language!) {
		categoryBreadcrumbsWithUrl(url: $url, language: $language) {
			name
			url
		}
		singleCategoryWithUrl(url: $url, language: $language) {
			id
			name
			descriptions {
				id
				title
				contents {
					id
					text
					quotes {
						id
						title
						date
						locationPage
						locationUrl
						source {
							label
							url
						}
					}
				}
			}
			children {
				id
				name
				url
				leaf
			}
			neighbors {
				id
				name
				url
				leaf
			}
		}
	}
`;
