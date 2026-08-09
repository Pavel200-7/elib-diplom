import client from '../client/client'

export const issueCopy = (userId, copyId) => {
    return client.post('/api/v1/circulation/issue', null, {
        params: { userId, copyId }
    })
}

export const returnCopy = (bookingId) => {
    return client.put(`/api/v1/circulation/return/${bookingId}`)
}
